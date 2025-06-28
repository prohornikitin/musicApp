package com.example.musicapp.domain.logic.impure.iface.storage.read

import com.example.musicapp.domain.data.SongId

fun interface SongThumbnailStorage {
    fun getIcon(id: SongId): ByteArray?
}