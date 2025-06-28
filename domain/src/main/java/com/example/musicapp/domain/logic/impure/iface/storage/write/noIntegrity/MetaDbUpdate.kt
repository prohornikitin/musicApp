package com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongId

fun interface MetaDbUpdate {
    fun updateMetadata(id: SongId, newMeta: Map<MetaKey, String>)
}