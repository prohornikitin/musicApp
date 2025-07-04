package com.example.musicapp.domain.logic.impure.iface.storage.v2.read

import com.example.musicapp.domain.data.SongId

fun interface SongThumbnailStorage {
    fun get(id: SongId): ByteArray?
}