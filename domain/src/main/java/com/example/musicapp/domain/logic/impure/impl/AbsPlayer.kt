package com.example.musicapp.domain.logic.impure.impl

import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.MusicPlayer
import com.example.musicapp.domain.logic.impure.iface.MusicPlayer.PlaybackState
import com.example.musicapp.domain.logic.impure.iface.MusicPlayer.RepeatMode
import kotlin.collections.getOrNull

abstract class AbsPlayer : MusicPlayer {
    private val mListeners: MutableSet<MusicPlayer.Listener> = mutableSetOf()
    val listeners: Set<MusicPlayer.Listener>
        get() = mListeners

    override var repeatMode: RepeatMode = RepeatMode.OFF
        set(value) {
            if(field == value) {
                return
            }
            field = value
            listeners.forEach { it.onRepeatModeChanged(value) }
        }

    override var shuffleMode: Boolean = false
        set(value) {
            if(field == value) {
                return
            }
            field = value
            listeners.forEach { it.onShuffleModeEnabledChanged(value) }
        }

    override var playlist: List<SongId> = emptyList()
        protected set(value) {
            if(field == value) {
                return
            }
            field = value
            currentSong = value.getOrNull(0)
        }

    override var state: PlaybackState = PlaybackState.Idle
        protected set(value) {
            if(field == value) {
                return
            }
            field = value
            if(value == PlaybackState.Playing) {
                listeners.forEach { it.onIsPlayingChanged(true) }
            } else {
                listeners.forEach { it.onIsPlayingChanged(false) }
            }
            if(value == PlaybackState.Loading) {
                listeners.forEach { it.onIsLoadingChanged(true) }
            } else {
                listeners.forEach { it.onIsLoadingChanged(false) }
            }
        }

    override var currentSong: SongId? = null
        protected set(id) {
            field = id
            listeners.forEach { it.onSongTransition(id) }
        }

    protected var lengthMs: Long = 0
    override var positionMs: Long = 0
        protected set(value) {
            field = value
            listeners.forEach { it.onPositionChange(positionMs, lengthMs) }
        }

    override fun addListener(listener: MusicPlayer.Listener) {
        mListeners.add(listener)
    }

    override fun removeListener(listener: MusicPlayer.Listener) {
        mListeners.remove(listener)
    }

    override fun changePlaylist(playlist: List<SongId>) {
        this.playlist = playlist
        play()
    }

    override fun seek(positionMs: Long) {
        this.positionMs = positionMs
    }
}