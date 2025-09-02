package com.example.musicapp.domain.logic.impure.iface.player

import com.example.musicapp.domain.data.SongId

sealed interface PlaybackState {
    object Idle : PlaybackState
    object Loading: PlaybackState
    data class Paused(val songIndex: Int, val song: SongId): PlaybackState
    data class Playing(val songIndex: Int, val song: SongId): PlaybackState
//        enum class Reason {
//            Unknown,
//        }
}
