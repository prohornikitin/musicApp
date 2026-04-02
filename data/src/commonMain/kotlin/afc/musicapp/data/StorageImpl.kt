package afc.musicapp.data

import afc.musicapp.data.impure.iface.LowLevelConfig
import afc.musicapp.domain.entities.ConfigParam
import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SongCardTemplates
import afc.musicapp.domain.entities.SongCardText
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.logic.impure.iface.FileMetaParser
import afc.musicapp.domain.logic.impure.iface.FileMetaUpdate
import afc.musicapp.domain.logic.impure.iface.MemoryCache
import afc.musicapp.domain.logic.impure.iface.SongFileImport
import afc.musicapp.data.impure.iface.db.DbQueryInterpreter
import afc.musicapp.data.impure.iface.db.DbQueryInterpreter.TransactionResult
import afc.musicapp.domain.logic.impure.iface.storage.read.MetaRead
import afc.musicapp.domain.logic.impure.iface.storage.read.SongCardTextRead
import afc.musicapp.domain.logic.impure.iface.storage.TemplatesConfig
import afc.musicapp.domain.logic.impure.iface.storage.write.MetaFormatConfigUpdate
import afc.musicapp.domain.logic.impure.iface.storage.write.MetadataEdit
import afc.musicapp.domain.logic.pure.applyTemplates
import afc.musicapp.domain.logic.pure.mergeMultiValueMetas
import afc.musicapp.data.pure.sql.mainDb.Meta
import afc.musicapp.data.pure.sql.mainDb.Songs
import afc.musicapp.data.pure.sql.mainDb.Templates
import afc.musicapp.domain.logic.impure.iface.SongRemove
import afc.musicapp.domain.logic.pure.toMetasBySong
import io.ktor.util.collections.ConcurrentMap
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath

class StorageImpl(
    private val db: DbQueryInterpreter,
    private val metaParser: FileMetaParser,
    private val fileMetaUpdate: FileMetaUpdate,
    private val config: LowLevelConfig,
    private val fileSystem: FileSystem,
) : MetaRead, MetadataEdit, SongCardTextRead, TemplatesConfig, MetaFormatConfigUpdate, SongFileImport, SongRemove {
    init {
        MemoryCache.add {
            textCacheBySong.clear()
        }
    }

    override suspend fun getAllFields(song: SongId): List<Pair<MetaKey, String>> {
        return db.executeOrDefault(Meta.getAllMetas(listOf(song))).map {
            Pair(it.second, it.third)
        }
    }

    override suspend fun getFields(
        song: SongId,
        fields: Set<MetaKey>
    ): List<Pair<MetaKey, String>> {
        return getFields(listOf(song), fields).map {
            Pair(it.second, it.third)
        }
    }

    override suspend fun setDelimiter(delimiter: String) {
        db.transaction {
            val configStorage = ConfigStorageInDb(this)
            val template = config.get(configStorage, ConfigParam.templates)
            config.put(configStorage, ConfigParam.metaDelimiter, delimiter)
            regenTemplate(template, delimiter)
            TransactionResult.COMMIT
        }
    }


    override suspend fun replaceAllTagsForSong(song: SongId, tags: List<Pair<MetaKey, String>>) {
        val configStorage = ConfigStorageInDb(db)
        val templates = config.get(configStorage, ConfigParam.templates)
        val delimiter = config.get(configStorage, ConfigParam.metaDelimiter)
        val newText = applyTemplates(templates, mergeMultiValueMetas(tags, delimiter))
        db.transaction {
            executeOrThrow(Meta.clearMeta(song))
            executeOrThrow(Meta.insertMeta(song, tags))
            executeOrThrow(Templates.updateGeneratedTemplates(mapOf(song to newText)))
            textCacheBySong[song] = newText
            TransactionResult.COMMIT
        }
    }

    override suspend fun getFields(songs: List<SongId>, fields: Set<MetaKey>) =
        db.executeOrDefault(Meta.getMetas(songs, fields))

    private val textCacheBySong: ConcurrentMap<SongId, SongCardText> = ConcurrentMap()

    override suspend fun get(ids: List<SongId>): Map<SongId, SongCardText> {
        val toLoad = ids - textCacheBySong.keys
        if (!toLoad.isEmpty()) {
            textCacheBySong.putAll(db.executeOrDefault(Templates.getSongCardTextFor(toLoad)))
        }
        return textCacheBySong
    }

    override suspend fun getTemplates(): SongCardTemplates {
        return config.get(ConfigStorageInDb(db), ConfigParam.templates)
    }

    override suspend fun updateTemplates(new: SongCardTemplates) {
        val delimiter = config.get(ConfigStorageInDb(db), ConfigParam.metaDelimiter)
        db.transaction {
            config.put(ConfigStorageInDb(this), ConfigParam.templates, new)
            regenTemplate(new, delimiter)
            TransactionResult.COMMIT
        }
    }

    private suspend fun DbQueryInterpreter.regenTemplate(new: SongCardTemplates, delimiter: String) {
        val usedKeys = new.getUsedKeys()
        val pages = (this.executeOrDefault(Songs.countAll) ?: 0) / PAGE_SIZE
        for (page in 0..pages) {
            val tags = executeOrThrow(Meta.getMetasForAllSongsPageableBySong(
                keys = usedKeys,
                page = page,
                pageSize = PAGE_SIZE,
            ))
            val newTemplatesBySong = tags
                .toMetasBySong()
                .mapValues { applyTemplates(new, mergeMultiValueMetas(it.value, delimiter)) }
            executeOrThrow(Templates.updateGeneratedTemplates(newTemplatesBySong))
        }
        textCacheBySong.clear()
    }

    override suspend fun importIfSongNotExistsAlready(
        file: Path,
        deleteMetaFromFileAfterImport: Boolean
    ) {
        val meta = metaParser.getFullMetaFromFile(file)
        db.transaction {
            val newSongId = executeOrDefault(Songs.insertSongFileIfNotExists(file.toString()))
                ?.let(::SongId)
                ?: return@transaction TransactionResult.ROLLBACK
            val templates = config.get(ConfigStorageInDb(this), ConfigParam.templates)
            executeOrThrow(Meta.insertMeta(newSongId, meta.properties.toList()))
            executeOrThrow(Templates.updateGeneratedTemplates(
                mapOf(newSongId to applyTemplates(templates, meta.properties)))
            )
            if (deleteMetaFromFileAfterImport) {
                fileMetaUpdate.clearMeta(file)
            }
            TransactionResult.COMMIT
        }
    }

    override suspend fun reloadDataFromFile(file: Path) {
        val meta = metaParser.getFullMetaFromFile(file)
        db.transaction {
            val newSongId = executeOrDefault(Songs.getSongIdByFile(file.toString()))
                ?.let(::SongId)
                ?: return@transaction TransactionResult.COMMIT
            val templates = config.get(ConfigStorageInDb(this), ConfigParam.templates)
            executeOrThrow(Meta.clearMeta(newSongId))
            executeOrThrow(Meta.insertMeta(newSongId, meta.properties.toList()))
            executeOrThrow(Templates.updateGeneratedTemplates(
                mapOf(newSongId to applyTemplates(templates, meta.properties)))
            )
            TransactionResult.COMMIT
        }
    }

    override suspend fun remove(id: SongId) {
        var path: Path? = null
        db.transaction {
            path = executeOrThrow(Songs.getFileBySongId(id))?.toPath()
            if (path == null) {
                return@transaction TransactionResult.ROLLBACK
            }
            executeOrThrow(Templates.removeTemplate(id))
            executeOrThrow(Meta.clearMeta(id))
            executeOrThrow(Songs.removeFileFromDb(id))
            TransactionResult.COMMIT
        }
        path?.let { fileSystem.delete(it) }
    }

    companion object {
        const val PAGE_SIZE = 100L
    }
}