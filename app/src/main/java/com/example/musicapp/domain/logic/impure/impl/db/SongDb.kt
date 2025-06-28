package com.example.musicapp.domain.logic.impure.impl.db

import android.content.ContentValues
import android.content.Context
import android.database.sqlite.SQLiteConstraintException
import android.database.sqlite.SQLiteDatabase
import androidx.core.database.getBlobOrNull
import com.example.config.SongCardText
import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SearchQuery
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.storage.read.GeneratedTemplatesStorage
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.GeneratedTemplatesEdit
import com.example.musicapp.domain.logic.impure.iface.storage.read.SongFileStorage
import com.example.musicapp.domain.logic.impure.iface.SongSearch
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.SongFileDbRemove
import com.example.musicapp.domain.logic.impure.iface.storage.read.SongThumbnailStorage
import com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed.SongThumbnailUpdate
import com.example.musicapp.domain.logic.impure.iface.storage.read.MetaStorage
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.MetaDbRemove
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.MetaDbUpdate
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.SongFileDbAdd
import com.example.musicapp.domain.logic.pure.sql.DbSql
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.CREATE_FILE_TABLE
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.CREATE_GEN_TEMPLATE_TABLE
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.CREATE_ICON_TABLE
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.CREATE_META_TABLE
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.DATABASE_VERSION
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.GET_ALL_SONG_IDS
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.SONG_FILE_FILE
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.TABLE_SONG_FILE
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.getFileBySongId
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.getFilesBySongId
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.getIconFor
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.getMetasForAll
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.insertIcon
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.replaceMetas
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.upgrade
import java.io.File


class SongDb private constructor(
    context: Context,
) : SqliteDb(context, "songs.db", DATABASE_VERSION),
    SongSearch,
    SongFileStorage,
    SongFileDbRemove,
    MetaDbUpdate,
    MetaStorage,
    GeneratedTemplatesStorage,
    GeneratedTemplatesEdit,
    SongThumbnailStorage,
    SongThumbnailUpdate,
    SongFileDbAdd,
    MetaDbRemove
{
    companion object {
        private var instance: SongDb? = null

        fun getInstance(context: Context) = synchronized(this) {
            if (instance == null) {
                instance = SongDb(context)
            }
            return@synchronized instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.run {
            execSQL(CREATE_FILE_TABLE)
            execSQL(CREATE_ICON_TABLE)
            execSQL(CREATE_META_TABLE)
            execSQL(CREATE_GEN_TEMPLATE_TABLE)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        upgrade(oldVersion).forEach {
            db?.execSQL(it.toString())
        }
    }

    override fun search(
        initialList: List<SongId>,
        query: SearchQuery,
    ): List<SongId> {
        if(initialList.isEmpty() || query.isEmpty()) {
            return initialList
        }
        return selectMultiple(
            SongsDbSql.search(initialList, query),
        ) {
            SongId(getLong(0))
        }
    }

    override fun listSongs(): List<SongId> {
        return selectMultiple(GET_ALL_SONG_IDS) {
            SongId(getLong(0))
        }
    }

    override fun getFile(id: SongId): File? {
        return selectOne(
            getFileBySongId(id)
        ) {
            return@selectOne File(getString(0))
        }
    }

    override fun getFiles(ids: List<SongId>): Map<SongId, File> {
        return selectMultiple(
            getFilesBySongId(ids)
        ) {
            Pair(SongId(getLong(0)), File(getString(1)))
        }.toMap()
    }

    override fun getMetadataFields(fields: Set<MetaKey>): Map<SongId, Map<MetaKey, String>> {
        return selectMultiple(getMetasForAll(fields)) {
            Triple(SongId(getLong(0)), MetaKey(getString(1)), getString(2))
        }.groupBy {
            it.first
        }.mapValues {
            it.value.associate {
                Pair(it.second, it.third)
            }
        }
    }

    override fun putNewIfNotExists(file: File): SongId? {
        try {
            val id = SongId(
                writableDatabase.insertOrThrow(
                    TABLE_SONG_FILE,
                    null,
                    ContentValues().apply {
                        put(SONG_FILE_FILE, file.path)
                    },
                )
            )
            if (id.raw == -1L) {
                return null
            }
            return id
        } catch (_: SQLiteConstraintException) {
            return null
        }
    }

    override fun removeFile(id: SongId) {
        val file = getFile(id)
        file?.delete()
        writableDatabase?.execSQL(SongsDbSql.removeFileFromDb(id))
    }

    override fun updateMetadata(id: SongId, newMeta: Map<MetaKey, String>) {
        if(newMeta.isEmpty()) {
            return
        }
        writableDatabase.execSQL(replaceMetas(id, newMeta))
    }

    override fun updateStoredTemplates(templates: Map<SongId, SongCardText>) {
        writableDatabase.execSQL(SongsDbSql.updateGeneratedTemplates(templates))
    }

    override fun getSongCardText(id: SongId): SongCardText {
        return selectOne(SongsDbSql.getTemplateTexts(id)) {
            SongCardText(getString(0), getString(1))
        } ?: SongCardText("", "")
    }

    override fun getIcon(id: SongId): ByteArray? {
        return selectOne(getIconFor(id)) {
            return@selectOne getBlobOrNull(0)
        }
    }

    override fun saveIcon(id: SongId, data: ByteArray?) {
        writableDatabase.apply {
            val stmt = compileStatement(insertIcon)
            stmt.bindLong(1, id.raw)
            stmt.bindBlob(2, data)
            stmt.executeInsert()
        }
    }

    override fun removeAllMeta(id: SongId) {
        writableDatabase?.execSQL(SongsDbSql.removeAllMeta(id))
    }

    override fun remove(id: SongId, keys: List<MetaKey>) {
        writableDatabase?.execSQL(SongsDbSql.removeMeta(id, keys))
    }
}