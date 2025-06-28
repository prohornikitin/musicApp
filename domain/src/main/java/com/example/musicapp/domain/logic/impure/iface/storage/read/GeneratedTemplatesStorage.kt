package com.example.musicapp.domain.logic.impure.iface.storage.read

import com.example.config.SongCardText
import com.example.musicapp.domain.data.SongId

fun interface GeneratedTemplatesStorage {
    fun getSongCardText(id: SongId): SongCardText
}