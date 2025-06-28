package com.example.musicapp.domain.data

class SongCardData(
    val id: SongId,
    val mainText: String,
    val bottomText: String = "",
    val iconBitmap: ByteArray? = null,
)