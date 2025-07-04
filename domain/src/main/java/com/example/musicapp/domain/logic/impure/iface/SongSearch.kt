package com.example.musicapp.domain.logic.impure.iface

import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.data.SearchQuery

interface SongSearch {
    fun search(
        query: SearchQuery,
    ): List<SongId>

    fun allSongs(): List<SongId>
}