package com.example.musicapp.domain.data

import kotlinx.serialization.Serializable

@Serializable
data class SongCardText(
    val main: String = "",
    val sub: String = "",
)