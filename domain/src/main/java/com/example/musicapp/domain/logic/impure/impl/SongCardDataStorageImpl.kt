package com.example.musicapp.domain.logic.impure.impl

import com.example.musicapp.domain.data.SongCardData
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.impure.iface.storage.v2.read.SongCardDataStorage
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSql

class SongCardDataStorageImpl(
    private val db: DbQueryInterpreter,
) : SongCardDataStorage {
    override fun get(ids: List<SongId>): List<SongCardData> = db.execute(MainDbSql.getSongCardDataFor(ids))
}