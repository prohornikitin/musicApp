package com.example.musicapp.domain.logic.impure.impl.storage.l1

import com.example.musicapp.domain.data.SearchQuery
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.SongSearch
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import com.example.musicapp.domain.logic.pure.sql.mainDb.SongsQuery
import javax.inject.Inject

class SongSearchImpl @Inject constructor(
    private val db: DbQueryInterpreter,
) : SongSearch {
    override fun search(query: SearchQuery): List<SongId> = db.execute(SongsQuery.search(query))
    override fun allSongs(): List<SongId> = db.execute(MainDbSetup.getAllSongIds)
}