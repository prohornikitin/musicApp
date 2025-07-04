package com.example.musicapp.domain.logic.impure.iface.storage.write

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongId

interface MetadataEdit {
    fun removeByKeys(id: SongId, keys: Set<MetaKey>)
    fun removeAllExceptOfAllowedKeys(id: SongId, allowed: Set<MetaKey>)
    fun update(id: SongId, data: Map<MetaKey, String>)
}