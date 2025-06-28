package com.example.musicapp.domain.logic.impure.iface

import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.data.SearchQuery

interface SongSearch {
    fun search(
        initialList: List<SongId>,
        query: SearchQuery,
    ): List<SongId>

    fun listSongs(): List<SongId>
}