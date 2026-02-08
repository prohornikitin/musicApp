package afc.musicapp.domain.logic.impure.iface.player

import afc.musicapp.domain.entities.SongId


interface PlayerPlaylistChange {
    fun changePlaylistFully(new: List<SongId>)
    fun removeFromPlaylist(index: Int)
    fun movePlaylistItems(fromIndex: Int, toIndex: Int)
}