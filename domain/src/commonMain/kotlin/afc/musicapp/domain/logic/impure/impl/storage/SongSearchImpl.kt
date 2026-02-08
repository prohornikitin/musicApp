package afc.musicapp.domain.logic.impure.impl.storage

import afc.musicapp.domain.entities.SearchQuery
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.logic.impure.iface.SongSearch
import afc.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import afc.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import afc.musicapp.domain.logic.pure.sql.mainDb.SongsQuery

class SongSearchImpl(
    private val db: DbQueryInterpreter,
) : SongSearch {
    override suspend fun search(query: SearchQuery): List<SongId> = db.executeOrDefault(SongsQuery.search(query))
    override suspend fun allSongs(): List<SongId> = db.executeOrDefault(MainDbSetup.getAllSongIds)
}