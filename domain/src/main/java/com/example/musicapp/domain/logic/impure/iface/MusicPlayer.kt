package com.example.musicapp.domain.logic.impure.iface

import com.example.musicapp.domain.data.SongId

interface MusicPlayer {
    enum class RepeatMode {
        OFF,
        REPEAT_ALL,
        REPEAT_ONE,
    }

    enum class PlaybackState {
        Idle,
        Loading,
        Paused,
        Playing,
    }

    enum class Error {
        Unknown,
    }

    interface Listener {
        fun onSongTransition(id: SongId?) {}
        fun onIsLoadingChanged(isLoading: Boolean) {}
        fun onIsPlayingChanged(isPlaying: Boolean) {}
        fun onRepeatModeChanged(repeatMode: RepeatMode) {}
        fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {}
        fun onPositionChange(positionMs: Long, lengthMs: Long) {}
        fun onError(error: Error) {}
    }

    fun addListener(listener: Listener)
    fun removeListener(listener: Listener)

    var repeatMode: RepeatMode
    var shuffleMode: Boolean
    val playlist: List<SongId>
    val state: PlaybackState
    val currentSong: SongId?
    val positionMs: Long

    fun changePlaylist(playlist: List<SongId>)
    fun seek(positionMs: Long)
    fun play()
    fun pause()
}
//
//interface MusicPlayer {
//    interface Listener {
//        fun onStateChange(diff: PlayerStateDiff, newState: PlayerState) {}
//        fun onError(error: Error) {}
//    }
//
//    enum class Error {
//        Unknown,
//    }
//
//    val state: PlayerState
//
//    fun changeState(diff: PlayerStateDiff)
//
//    fun addListener(listener: Listener)
//    fun removeListener(listener: Listener)
//}

