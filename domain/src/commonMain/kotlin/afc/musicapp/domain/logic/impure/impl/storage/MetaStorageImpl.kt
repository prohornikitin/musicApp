package afc.musicapp.domain.logic.impure.impl.storage

import afc.musicapp.domain.entities.ConfigParam
import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SongCardTemplates
import afc.musicapp.domain.entities.SongCardText
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.logic.impure.iface.MergedMetaRead
import afc.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import afc.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter.TransactionResult
import afc.musicapp.domain.logic.impure.iface.storage.KvConfig
import afc.musicapp.domain.logic.impure.iface.storage.read.MetaFormatConfigRead
import afc.musicapp.domain.logic.impure.iface.storage.read.MetaRead
import afc.musicapp.domain.logic.impure.iface.storage.transactional.write.MetadataEdit
import afc.musicapp.domain.logic.pure.applyTemplate
import afc.musicapp.domain.logic.pure.applyTemplates
import afc.musicapp.domain.logic.pure.mergeMultiValueMetas
import afc.musicapp.domain.logic.pure.splitMultiValueMetas
import afc.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import afc.musicapp.domain.logic.pure.sql.mainDb.Meta
import afc.musicapp.domain.logic.pure.toMetasBySong
import kotlinx.coroutines.selects.select

class MetaStorageImpl(
    private val db: DbQueryInterpreter,
    private val config: KvConfig,
) : MetaRead, MetadataEdit {
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

    override suspend fun replaceAllTagsForSong(song: SongId, tags: List<Pair<MetaKey, String>>) {
        val templates = config.get(db, ConfigParam.templates)
        val delimiter = config.get(db, ConfigParam.metaDelimiter)
        val newText = applyTemplates(templates, mergeMultiValueMetas(tags, delimiter))
        db.transaction {
            executeOrThrow(Meta.clearMeta(song))
            executeOrThrow(Meta.insertMeta(song, tags))
            executeOrThrow(MainDbSetup.updateGeneratedTemplates(mapOf(song to newText)))
            TransactionResult.COMMIT
        }
    }

    override suspend fun updateAllTagsForSongAndUpdateText(
        song: SongId,
        tags: List<Pair<MetaKey, String>>,
        newText: SongCardText
    ) {
        TODO("Not yet implemented")
    }


    override suspend fun getFields(songs: List<SongId>, fields: Set<MetaKey>) =
        db.executeOrDefault(Meta.getMetas(songs, fields))

    companion object {
        const val CHUNK_SIZE = 100
    }
}