package com.example.config

import kotlinx.serialization.Serializable

@Serializable
data class SongCardTemplates(
    val main: String = "",
    val bottom: String = "",
)