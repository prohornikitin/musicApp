package com.example.musicapp.domain.data

import kotlinx.serialization.Serializable

@Serializable
@JvmInline
value class SongId(val raw: Long)