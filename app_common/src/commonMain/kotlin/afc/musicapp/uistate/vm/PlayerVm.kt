package afc.musicapp.uistate.vm

import afc.musicapp.domain.entities.SongCardData
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.logic.impure.iface.Dispatchers
import afc.musicapp.domain.logic.impure.iface.player.PlaybackState.Idle
import afc.musicapp.domain.logic.impure.iface.player.PlaybackState.Loading
import afc.musicapp.domain.logic.impure.iface.player.PlaybackState.Paused
import afc.musicapp.domain.logic.impure.iface.player.PlaybackState.Playing
import afc.musicapp.domain.logic.impure.iface.player.Player
import afc.musicapp.domain.logic.impure.iface.SongCardDataRetrieve
import afc.musicapp.mapState
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.runBlocking

class PlayerVm constructor(
    private val player: Player,
    private val songCardDataStorage: SongCardDataRetrieve,
    dispatchers: Dispatchers,
) : BaseVm(dispatchers) {
    private val currentSongId =
        player.playbackState.mapState {
            when(it) {
                Idle,
                Loading -> null
                is Paused -> it.song
                is Playing -> it.song
            }
        }

    private val currentSongIndex =
        player.playbackState.mapState {
            when(it) {
                Idle,
                Loading -> null
                is Paused -> it.songIndex
                is Playing -> it.songIndex
            }
        }

    val currentSongCard: StateFlow<SongCardData?> = currentSongId.mapState {
        runBlocking {
            it?.let { it1 -> songCardDataStorage.get(it1) }
        }
    }

    val isPlaying: StateFlow<Boolean> = player.playbackState.mapState {
        it is Playing
    }

    fun changePlaylist(list: List<SongId>, index: Int) {
        if(player.playlist.value != list) {
            player.changePlaylistFully(list)
        }
        player.playIndex(index)
    }

    fun togglePlayPause() {
        if (isPlaying.value) {
            player.pause()
        } else {
            player.play()
        }
    }
}