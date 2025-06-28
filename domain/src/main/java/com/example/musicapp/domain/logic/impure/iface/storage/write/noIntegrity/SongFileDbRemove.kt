package com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity

import com.example.musicapp.domain.data.SongId

fun interface SongFileDbRemove {
    fun removeFile(id: SongId)
}