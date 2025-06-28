package com.example.musicapp.uistate

import com.example.musicapp.domain.data.SongCardData

data class UiState(
    val allSongs: List<SongCardData> = emptyList(),
    val afterSearch: List<SongCardData> = emptyList(),
    val isLoading: Boolean = false,
)