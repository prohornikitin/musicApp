package com.example.musicapp.domain.logic.impure.iface.storage.v2.read

import com.example.musicapp.domain.data.SongCardData
import com.example.musicapp.domain.data.SongId

interface SongCardDataStorage {
    fun get(ids: List<SongId>): List<SongCardData>
    fun get(id: SongId): SongCardData? = get(listOf(id)).getOrNull(0)
}