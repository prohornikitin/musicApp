package com.example.musicapp

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.PositionInfo
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.common.Player.STATE_BUFFERING
import androidx.media3.common.Player.STATE_READY
import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.FormattedMetaRead
import com.example.musicapp.domain.logic.impure.iface.player.Player
import com.example.musicapp.domain.logic.impure.iface.player.MusicPlayerState
import com.example.musicapp.domain.logic.impure.iface.player.NextSongStrategy
import com.example.musicapp.domain.logic.impure.iface.player.PlaybackState
import com.example.musicapp.domain.logic.impure.iface.player.RepeatMode
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.SongCardTextStorage
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.SongFiles
import com.example.musicapp.domain.logic.impure.impl.extractMediaMetadata
import com.example.musicapp.domain.logic.impure.impl.songId
import com.example.musicapp.domain.toNotNullMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


class PlayerImpl(
    private val getPlatformPlayer: suspend () -> androidx.media3.common.Player,
    private val metaStorage: FormattedMetaRead,
    private val songCardTextStorage: SongCardTextStorage,
    private val fileStorage: SongFiles,
) : Player {
    private var player: androidx.media3.common.Player? = null

    private val scope = CoroutineScope(Dispatchers.Default)

    private fun usePPlayer(f: androidx.media3.common.Player.() -> Unit) {
        scope.launch {
            if (player == null) {
                player = getPlatformPlayer()
            }
            withContext(Dispatchers.Main) {
                player!!.f()
            }
        }
    }

    private fun Int.asRepeatModeEnum() = when(this) {
        REPEAT_MODE_ONE -> RepeatMode.REPEAT_ONE
        REPEAT_MODE_ALL -> RepeatMode.REPEAT_ALL
        else -> RepeatMode.OFF
    }

    private fun RepeatMode.toMedia3PlayerInt() = when(this) {
        RepeatMode.OFF -> REPEAT_MODE_OFF
        RepeatMode.REPEAT_ONE -> REPEAT_MODE_ONE
        RepeatMode.REPEAT_ALL -> REPEAT_MODE_ALL
    }

    override fun play() = usePPlayer { play() }
    override fun pause() = usePPlayer { pause() }
    override fun playNext() = usePPlayer { seekToNextMediaItem() }
    override fun playPrevious() = usePPlayer { seekToPreviousMediaItem() }

    override fun setNextSongStrategy(new: NextSongStrategy) = usePPlayer {
        nextSongStrategy.value = new
        repeatMode = new.repeat.toMedia3PlayerInt()
        shuffleModeEnabled = new.shuffle
    }

    override fun movePlaylistItems(fromIndex: Int, toIndex: Int) = usePPlayer {
        moveMediaItem(fromIndex, toIndex)
    }

    override fun removeFromPlaylist(index: Int) = usePPlayer {
        removeMediaItem(index)
    }

    override fun changePlaylistFully(new: List<SongId>) = usePPlayer {
        //TODO: optimize
        val meta = metaStorage.getFields(
            new,
            setOf(
                MetaKey.TITLE,
                MetaKey.ARTIST,
                MetaKey.ALBUM,
                MetaKey.ALBUM_ARTIST,
            ),
        )
        val songCardData = songCardTextStorage.get(new).associateBy { it.id }
        setMediaItems(new.map {
            MediaItem.Builder()
                .setMediaMetadata(extractMediaMetadata(songCardData[it]!!, meta[it].toNotNullMap()))
                .setUri(fileStorage.getById(it)?.toUri())
                .setMediaId(it.raw.toString())
                .build()
        })
        play()
    }

    override fun seekTo(newPosition: Duration) = usePPlayer {
        seekTo(newPosition.inWholeMilliseconds)
    }

    override fun seekDelta(delta: Duration) = usePPlayer {
        seekTo(currentPosition - delta.inWholeMilliseconds)
    }

    private fun androidx.media3.common.Player.updatePlaybackState() {
        this@PlayerImpl.playbackState.value = buildPlaybackState()
    }

    private fun androidx.media3.common.Player.buildPlaybackState(): PlaybackState {
        return when(playbackState) {
            STATE_BUFFERING -> PlaybackState.Loading
            STATE_READY -> {
                val song = currentMediaItem?.songId!!
                val index = currentMediaItemIndex
                if (isPlaying) {
                    PlaybackState.Playing(index, song)
                } else {
                    PlaybackState.Paused(index, song)
                }
            }
            else -> PlaybackState.Idle
        }
    }


    override val playlist: MutableStateFlow<List<SongId>> = MutableStateFlow(emptyList())

    override val nextSongStrategy: MutableStateFlow<NextSongStrategy> = MutableStateFlow(
        NextSongStrategy(RepeatMode.REPEAT_ONE, false)
    )

    init {
        usePPlayer {
            addListener(object: androidx.media3.common.Player.Listener {
                override fun onPlaybackStateChanged(playbackState: Int) {
                    updatePlaybackState()
                }

                override fun onRepeatModeChanged(repeatMode: Int) {
                    nextSongStrategy.value = nextSongStrategy.value.copy(repeatMode.asRepeatModeEnum())
                }

                override fun onShuffleModeEnabledChanged(enabled: Boolean) {
                    nextSongStrategy.value = nextSongStrategy.value.copy(shuffle = enabled)
                }

                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    updatePlaybackState()
                    positionListeners.forEach { it.positionChanged(currentPosition.milliseconds) }
                }

                override fun onIsLoadingChanged(isLoading: Boolean) {
                    updatePlaybackState()
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    updatePlaybackState()
                }

                override fun onPositionDiscontinuity(
                    oldPosition: PositionInfo,
                    newPosition: PositionInfo,
                    reason: Int
                ) {
                    if(getMediaItemAt(oldPosition.mediaItemIndex).songId != getMediaItemAt(newPosition.mediaItemIndex).songId) {
                        updatePlaybackState()
                        positionListeners.forEach { it.positionChanged(newPosition.positionMs.milliseconds) }
                    }
                    val old = oldPosition.positionMs.milliseconds
                    val new = oldPosition.positionMs.milliseconds
                    val diff = (new - old).absoluteValue
                    if (diff > positionUpdatePrecision) {
                        positionListeners.forEach { it.positionChanged(new) }
                    }
                }
            })
            playlist.value = (0..mediaItemCount-1).mapNotNull {
                getMediaItemAt(it).songId
            }
            nextSongStrategy.value = NextSongStrategy(repeatMode.asRepeatModeEnum(), shuffleModeEnabled)
            this@PlayerImpl.playbackState.value = buildPlaybackState()
        }
    }

    override val playbackState: MutableStateFlow<PlaybackState> = MutableStateFlow(PlaybackState.Idle)

    private val positionListeners: MutableList<MusicPlayerState.PositionListener> = mutableListOf()
    private var positionUpdatePrecision: Duration = Duration.INFINITE

    override fun subscribeOnPositionChange(
        precision: Duration,
        listener: MusicPlayerState.PositionListener
    ) {
        assert(!precision.isNegative())
        positionUpdatePrecision = minOf(positionUpdatePrecision, precision)
        positionListeners.add(listener)
        usePPlayer {
            listener.positionChanged(currentPosition.milliseconds)
        }
    }

    override fun unsubscribeOnPositionChange(listener: MusicPlayerState.PositionListener) {
        positionListeners.remove(listener)
    }
}