package com.example.musicapp.domain.logic.impure.iface.player

data class NextSongStrategy(
    val repeat: RepeatMode,
    val shuffle: Boolean,
)