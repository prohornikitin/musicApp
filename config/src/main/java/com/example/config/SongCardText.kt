package com.example.config

import kotlinx.serialization.Serializable

@Serializable
data class SongCardText(
    val main: String = "",
    val sub: String = "",
)