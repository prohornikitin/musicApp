package com.example.musicapp

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.MusicPlayer
import com.example.musicapp.domain.logic.impure.iface.MusicPlayer.PlaybackState
import com.example.musicapp.domain.logic.impure.iface.MusicPlayer.RepeatMode
import com.example.musicapp.domain.logic.impure.impl.AbsPlayer
import com.google.common.util.concurrent.MoreExecutors


class PlayerImpl(val appContext: Context) : AbsPlayer() {
    private var mediaPlayer: Player? = null
    private fun useExoPlayer(block: (player: Player) -> Unit) {
        if(mediaPlayer != null) {
            block(mediaPlayer!!)
            return
        }
        val sessionToken = SessionToken(appContext, ComponentName(appContext, PlayerService::class.java))
        val controllerFuture = MediaController.Builder(appContext, sessionToken).buildAsync()
        controllerFuture.addListener({
            mediaPlayer = controllerFuture.get()
            block(mediaPlayer!!)
        }, MoreExecutors.directExecutor())
    }

    init {
        useExoPlayer { player ->
            player.addListener(object: Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    this@PlayerImpl.state = when(playbackState) {
                        Player.STATE_BUFFERING -> PlaybackState.Loading
                        Player.STATE_READY -> if(player.playWhenReady) {
                            PlaybackState.Playing
                        } else {
                            PlaybackState.Paused
                        }
                        else -> PlaybackState.Idle
                    }
                }

                override fun onRepeatModeChanged(repeatMode: Int) {
                    this@PlayerImpl.repeatMode = when(repeatMode) {
                        Player.REPEAT_MODE_ONE -> RepeatMode.REPEAT_ONE
                        Player.REPEAT_MODE_ALL -> RepeatMode.REPEAT_ALL
                        else -> RepeatMode.OFF
                    }
                }

                override fun onShuffleModeEnabledChanged(enabled: Boolean) {
                    this@PlayerImpl.shuffleMode = enabled
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    this@PlayerImpl.currentSong = mediaItem?.mediaId?.toLongOrNull()?.let { SongId(it) }
                }
            })
            addListener(object : MusicPlayer.Listener {
                override fun onShuffleModeEnabledChanged(shuffleModeEnabled: Boolean) {
                    super.onShuffleModeEnabledChanged(shuffleModeEnabled)
                }
            })
        }
    }

    override fun play() {
        useExoPlayer { play() }
    }

    override fun pause() {
        useExoPlayer { pause() }
    }
}