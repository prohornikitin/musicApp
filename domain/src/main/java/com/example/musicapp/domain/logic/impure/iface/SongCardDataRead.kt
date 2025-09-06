package com.example.musicapp.domain.logic.impure.iface

import com.example.musicapp.domain.data.SongCardData
import com.example.musicapp.domain.data.SongId
import kotlinx.coroutines.flow.StateFlow

interface SongCardDataRead {
    fun getAsFlow(id: SongId): StateFlow<SongCardData>
    fun get(id: SongId): SongCardData
}