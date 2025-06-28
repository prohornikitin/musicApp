package com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed

import com.example.musicapp.domain.data.SongId

fun interface SongThumbnailUpdate {
    fun saveIcon(id: SongId, data: ByteArray?)
}