package com.example.musicapp.domain.logic.impure.impl

import com.example.musicapp.domain.data.SearchQuery
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.SongSearch
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSql

class SongSearchImpl(
    private val db: DbQueryInterpreter,
) : SongSearch {
    override fun search(query: SearchQuery): List<SongId> = db.execute(MainDbSql.search(query))
    override fun allSongs(): List<SongId> = db.execute(MainDbSql.getAllSongIds)
}