package afc.musicapp

import androidx.core.net.toUri
import androidx.media3.common.MediaItem
import androidx.media3.common.Player.PositionInfo
import androidx.media3.common.Player.REPEAT_MODE_ALL
import androidx.media3.common.Player.REPEAT_MODE_OFF
import androidx.media3.common.Player.REPEAT_MODE_ONE
import androidx.media3.common.Player.STATE_BUFFERING
import androidx.media3.common.Player.STATE_READY
import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.logic.impure.iface.Dispatchers
import afc.musicapp.domain.logic.impure.iface.MergedMetaRead
import afc.musicapp.domain.logic.impure.iface.SongCardDataRetrieve
import afc.musicapp.domain.logic.impure.iface.player.MusicPlayerState
import afc.musicapp.domain.logic.impure.iface.player.NextSongStrategy
import afc.musicapp.domain.logic.impure.iface.player.PlaybackState
import afc.musicapp.domain.logic.impure.iface.player.Player
import afc.musicapp.domain.logic.impure.iface.player.RepeatMode
import afc.musicapp.domain.logic.impure.iface.storage.read.SongFiles
import afc.musicapp.domain.logic.impure.impl.extractMediaMetadataIfAny
import afc.musicapp.domain.logic.impure.impl.songId
import afc.musicapp.domain.toNotNullMap
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import kotlin.time.Duration
import kotlin.time.Duration.Companion.milliseconds


class PlayerImpl(
    private val getPlatformPlayer: suspend () -> androidx.media3.common.Player,
    private val metaStorage: MergedMetaRead,
    private val songCardDataRepo: SongCardDataRetrieve,
    private val fileStorage: SongFiles,
    dispatchers: Dispatchers
) : Player {
    private var player: androidx.media3.common.Player? = null

    private val scope = CoroutineScope(dispatchers.default)

    private fun usePPlayer(f: suspend androidx.media3.common.Player.() -> Unit) {
        scope.launch {
            if (player == null) {
                player = getPlatformPlayer()
            }
            withContext(kotlinx.coroutines.Dispatchers.Main) {
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
    override fun playIndex(songIndex: Int) = usePPlayer { seekTo(songIndex, 0) }

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

    override fun changePlaylistFully(new: List<SongId>) {
        scope.launch {
            if (new == playlist.value) {
                return@launch
            }
            val playlistHead = new[0]
            val playlistTail = new.subList(1, new.size)
            usePPlayer {
                setMediaItem(loadMediaItemData(playlistHead))
                play()
            }

            usePPlayer {
                addMediaItems(playlistTail.map {
                    loadMediaItemData(it)
                })
            }

        }
    }

    private suspend fun loadMediaItemData(id: SongId): MediaItem {
        val meta = metaStorage.getFieldsMerged(
            id,
            setOf(
                MetaKey.TITLE,
                MetaKey.ARTIST,
                MetaKey.ALBUM,
                MetaKey.ALBUM_ARTIST,
            ),
        )
        val songCardData = songCardDataRepo.get(id)
        return MediaItem.Builder()
            .setMediaMetadata(
                extractMediaMetadataIfAny(
                    songCardData,
                    meta.toNotNullMap()
                )
            )
            .setUri(fileStorage.getById(id)?.toFile()?.toUri())
            .setMediaId(id.raw.toString())
            .build()
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