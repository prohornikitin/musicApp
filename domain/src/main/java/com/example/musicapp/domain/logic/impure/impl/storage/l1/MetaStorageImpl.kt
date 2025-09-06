package com.example.musicapp.domain.logic.impure.impl.storage.l1

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.MetaStorage
import com.example.musicapp.domain.logic.impure.iface.storage.l1.write.MetadataEdit
import com.example.musicapp.domain.logic.pure.sql.mainDb.Meta
import javax.inject.Inject

class MetaStorageImpl @Inject constructor(
    private val db: DbQueryInterpreter,
) : MetaStorage, MetadataEdit {
    override fun getAllFields(song: SongId): List<Pair<MetaKey, String>> {
        return db.execute(Meta.getAllMetas(listOf(song))).map {
            Pair(it.second, it.third)
        }
    }

    override fun getAllFields(songs: List<SongId>) = db.execute(Meta.getAllMetas(songs))

    override fun getFields(song: SongId, fields: Set<MetaKey>): List<Pair<MetaKey, String>> {
        return db.execute(Meta.getMetas(listOf(song), fields)).map {
            Pair(it.second, it.third)
        }
    }

    override fun getFields(songs: List<SongId>, fields: Set<MetaKey>) =
        db.execute(Meta.getMetas(songs, fields))

    override fun insertMeta(id: SongId, data: List<Pair<MetaKey, String>>): Long? =
        db.execute(Meta.insertMeta(id, data))

    override fun clearMeta(id: SongId) =
        db.execute(Meta.clearMeta(id))
}