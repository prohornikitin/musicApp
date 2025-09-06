package com.example.musicapp.domain.data

import kotlinx.serialization.Serializable

@Serializable
data class SongCardTemplates(
    val main: Template,
    val bottom: Template,
)