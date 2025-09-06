package com.example.musicapp.domain.logic.impure.iface.storage.l2.read

import com.example.musicapp.domain.data.SongId

interface ObservableThumbnailStorage {
    fun interface Listener {
        fun onThumbnailChanged(id: SongId, data: ByteArray)
    }
    val listeners: MutableCollection<Listener>
    fun get(id: SongId): ByteArray?
}