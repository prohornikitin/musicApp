package com.example.musicapp.uistate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.core.net.toUri
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.media3.common.MediaItem
import androidx.media3.common.Player
import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.PlayerFactory
import com.example.musicapp.domain.logic.impure.iface.storage.v2.read.SongFiles
import com.example.musicapp.domain.data.SongCardData
import com.example.musicapp.domain.logic.impure.iface.FormattedMetaStorage
import com.example.musicapp.domain.logic.impure.iface.storage.v2.read.SongCardDataStorage
import com.example.musicapp.domain.logic.impure.impl.extractMediaMetadata
import com.example.musicapp.domain.toNotNullMap
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerVm @Inject constructor(
    private val playerFactory: PlayerFactory,
    private val fileStorage: SongFiles,
    private val songCardDataStorage: SongCardDataStorage,
    private val metaStorage: FormattedMetaStorage,
) : ViewModel() {
    private lateinit var player: Player

    var currentSong by mutableStateOf<SongCardData?>(null)
        private set

    var isPlaying by mutableStateOf(false)
        private set

    fun changePlaylist(playlist: List<SongId>) {
        viewModelScope.launch {
            player.clearMediaItems()
            val meta = metaStorage.getFields(
                playlist,
                setOf(
                    MetaKey.TITLE,
                    MetaKey.ARTIST,
                    MetaKey.ALBUM,
                    MetaKey.ALBUM_ARTIST,
                ),
            )
            val songCardData = songCardDataStorage.get(playlist).associateBy { it.id }
            player.addMediaItems(playlist.map {
                MediaItem.Builder()
                    .setMediaMetadata(extractMediaMetadata(songCardData[it]!!, meta[it].toNotNullMap()))
                    .setUri(fileStorage.getById(it)?.toUri())
                    .setMediaId(it.raw.toString())
                    .build()
            })
            player.play()
        }
    }

    fun togglePause() {
        viewModelScope.launch {
            if(isPlaying) {
                player.pause()
            } else {
                player.play()
            }
        }
    }

    init {
        viewModelScope.launch {
            player = playerFactory.get()
            player.addListener(object : Player.Listener {
                override fun onMediaItemTransition(mediaItem: MediaItem?, reason: Int) {
                    currentSong = mediaItem?.mediaId?.toLongOrNull()?.let {
                        songCardDataStorage.get(SongId(it))
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    this@PlayerVm.isPlaying = isPlaying
                }
            })
        }
    }
}