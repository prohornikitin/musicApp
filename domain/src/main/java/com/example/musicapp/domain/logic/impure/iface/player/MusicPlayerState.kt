package com.example.musicapp.domain.logic.impure.iface.player

import com.example.musicapp.domain.data.SongId
import kotlinx.coroutines.flow.StateFlow
import kotlin.time.Duration
import kotlin.time.Duration.Companion.seconds

interface MusicPlayerState {
    val playlist: StateFlow<List<SongId>>
    val nextSongStrategy: StateFlow<NextSongStrategy>
    val playbackState: StateFlow<PlaybackState>

    //Will behave as StateFlow: emit last value just after subscribing
    fun subscribeOnPositionChange(precision: Duration = 10.seconds, listener: PositionListener)
    fun unsubscribeOnPositionChange(listener: PositionListener)
    fun interface PositionListener {
        fun positionChanged(position: Duration)
    }
}