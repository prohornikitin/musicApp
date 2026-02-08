package afc.musicapp.domain.logic.impure.iface

import afc.musicapp.domain.entities.SearchQuery
import afc.musicapp.domain.entities.SongId

interface SongSearch {
    suspend fun search(
        query: SearchQuery,
    ): List<SongId>

    suspend fun allSongs(): List<SongId>
}