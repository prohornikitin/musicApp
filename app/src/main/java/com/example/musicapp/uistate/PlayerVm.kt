package com.example.musicapp.uistate

import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.FormattedMetaStorage
import com.example.musicapp.domain.logic.impure.iface.player.PlaybackState.Idle
import com.example.musicapp.domain.logic.impure.iface.player.PlaybackState.Loading
import com.example.musicapp.domain.logic.impure.iface.player.PlaybackState.Paused
import com.example.musicapp.domain.logic.impure.iface.player.PlaybackState.Playing
import com.example.musicapp.domain.logic.impure.iface.player.Player
import com.example.musicapp.domain.logic.impure.iface.storage.v2.read.SongCardDataStorage
import com.example.musicapp.domain.logic.impure.impl.mapState
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.StateFlow
import javax.inject.Inject

@HiltViewModel
class PlayerVm @Inject constructor(
    private val player: Player,
    private val songCardDataStorage: SongCardDataStorage,
    private val metaStorage: FormattedMetaStorage,
) : ViewModel() {
    private val currentSongId =
        player.playbackState.mapState {
            when(it) {
                Idle,
                Loading -> null
                is Paused -> it.song
                is Playing -> it.song
            }
        }

    val currentSongCard = currentSongId.mapState {
        it?.let(songCardDataStorage::get)
    }

    val isPlaying: StateFlow<Boolean> = player.playbackState.mapState {
        it is Playing
    }

    fun changePlaylist(list: List<SongId>) {
        player.changePlaylistFully(list)
    }

    fun togglePlayPause() {
        if (isPlaying.value) {
            player.pause()
        } else {
            player.play()
        }
    }
}