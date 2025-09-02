package com.example.musicapp.domain.logic.impure.iface.player

import com.example.musicapp.domain.data.SongId


interface PlayerPlaylistChange {
    fun changePlaylistFully(new: List<SongId>)
    fun removeFromPlaylist(index: Int)
    fun movePlaylistItems(fromIndex: Int, toIndex: Int)
}