package com.example.musicapp.domain.logic.impure.iface.storage.read

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongId

fun interface MetaStorage {
    fun getMetadataFields(fields: Set<MetaKey>): Map<SongId, Map<MetaKey, String>>
}