package com.example.musicapp.uistate

import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.data.SongCardData

data class PlayerState(
    val playlist: List<SongId> = emptyList(),
    val currentSongIndex: Int? = null,
    val currentSongCard: SongCardData? = null,
    val isPlaying: Boolean = false,
)
