package com.example.musicapp.domain.logic.impure.iface.storage.l1.read

import com.example.musicapp.domain.data.SongId

interface ThumbnailStorageRead {
    fun getIconFile(song: SongId): ByteArray?
}