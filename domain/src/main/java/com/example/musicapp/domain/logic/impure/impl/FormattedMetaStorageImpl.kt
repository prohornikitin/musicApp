package com.example.musicapp.domain.logic.impure.impl

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.FormattedMetaRead
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.impure.iface.storage.l2.read.MetaFormatConfigRead
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.MetaStorage
import com.example.musicapp.domain.logic.pure.mergeMultiValueMetas
import com.example.musicapp.domain.logic.pure.sql.mainDb.Meta
import com.example.musicapp.domain.logic.pure.toMetasBySong
import javax.inject.Inject
import kotlin.collections.chunked

class FormattedMetaStorageImpl @Inject constructor(
    private val db: DbQueryInterpreter,
    private val metaStorage: MetaStorage,
    private val config: MetaFormatConfigRead
) : FormattedMetaRead {
    companion object {
        const val CHUNK_SIZE = 100
    }

    override fun getAllFields(song: SongId): Map<MetaKey, String> {
        val delimiter = config.getDelimiter()
        val metas = metaStorage.getAllFields(song)
        return mergeMultiValueMetas(metas, delimiter)
    }

    override fun getAllFields(songs: List<SongId>): Map<SongId, Map<MetaKey, String>> {
        val delimiter = config.getDelimiter()
        val result = mutableMapOf<SongId, Map<MetaKey, String>>()
        songs.chunked(CHUNK_SIZE).map { songsChunk ->
            val metasBySong = db.execute(Meta.getAllMetas(songsChunk)).toMetasBySong()
            metasBySong.forEach {
                result[it.key] = mergeMultiValueMetas(it.value, delimiter)
            }
        }
        return result
    }

    override fun getFields(
        song: SongId,
        fields: Set<MetaKey>
    ): Map<MetaKey, String> {
        val delimiter = config.getDelimiter()
        val metas = db.execute(Meta.getAllMetas(listOf(song))).map {
            Pair(it.second, it.third)
        }
        return mergeMultiValueMetas(metas, delimiter)
    }

    override fun getFields(
        songs: List<SongId>,
        fields: Set<MetaKey>
    ): Map<SongId, Map<MetaKey, String>> {
        val delimiter = config.getDelimiter()
        val result = mutableMapOf<SongId, Map<MetaKey, String>>()
        songs.chunked(CHUNK_SIZE).map { songsChunk ->
            val metasBySong = db.execute(Meta.getAllMetas(songsChunk)).toMetasBySong()
            metasBySong.forEach {
                result[it.key] = mergeMultiValueMetas(it.value, delimiter)
            }
        }
        return result
    }
}