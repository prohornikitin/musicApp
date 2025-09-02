package com.example.musicapp.domain.logic.impure.iface.player

import kotlin.time.Duration

interface PlayerControlSimple {
    fun setNextSongStrategy(new: NextSongStrategy)
    fun seekTo(newPosition: Duration)
    fun seekDelta(delta: Duration)
    fun playNext()
    fun playPrevious()
    fun play()
    fun pause()
}