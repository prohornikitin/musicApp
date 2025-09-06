package com.example.musicapp.domain.logic.impure.iface.storage.l1.read

import com.example.musicapp.domain.data.SongCardText
import com.example.musicapp.domain.data.SongId

interface SongCardTextStorage {
    fun get(ids: List<SongId>): Map<SongId, SongCardText>
    fun get(id: SongId): SongCardText? = get(listOf(id))[id]
}