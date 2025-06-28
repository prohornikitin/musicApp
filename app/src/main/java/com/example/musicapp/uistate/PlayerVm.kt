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
import com.example.musicapp.domain.logic.impure.iface.storage.read.GeneratedTemplatesStorage
import com.example.musicapp.domain.logic.impure.iface.PlayerFactory
import com.example.musicapp.domain.logic.impure.iface.storage.read.SongFileStorage
import com.example.musicapp.domain.logic.impure.iface.storage.read.MetaStorage
import com.example.musicapp.domain.data.SongCardData
import com.example.musicapp.domain.logic.impure.impl.toMediaMetadata
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class PlayerVm @Inject constructor(
    private val playerFactory: PlayerFactory,
    private val generatedTemplatesStorage: GeneratedTemplatesStorage,
    private val fileStorage: SongFileStorage,
    private val metaStorage: MetaStorage,
) : ViewModel() {
    private lateinit var player: Player

    var currentSong by mutableStateOf<SongCardData?>(null)
        private set

    var isPlaying by mutableStateOf(false)
        private set

    fun changePlaylist(playlist: List<SongId>) {
        viewModelScope.launch {
            player.clearMediaItems()
            val filesMap = fileStorage.getFiles(playlist)
            val meta = metaStorage.getMetadataFields(
                setOf(
                    MetaKey.TITLE,
                    MetaKey.ARTIST,
                    MetaKey.ALBUM,
                    MetaKey.ALBUM_ARTIST,
                )
            )
            player.addMediaItems(playlist.map {
                MediaItem.Builder()
                    .setMediaMetadata((meta[it] ?: emptyMap()).toMediaMetadata())
                    .setUri(filesMap[it]!!.toUri())
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
                        val id = SongId(it)
                        val templates = generatedTemplatesStorage.getSongCardText(id)
                        SongCardData(
                            id,
                            templates.main,
                            templates.sub,
                        )
                    }
                }

                override fun onIsPlayingChanged(isPlaying: Boolean) {
                    this@PlayerVm.isPlaying = isPlaying
                }
            })
        }
    }
}