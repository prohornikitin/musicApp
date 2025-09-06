package com.example.musicapp.domain.logic.impure.iface.storage.l1.write

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongId

interface MetadataEdit {
//    fun removeByKeys(id: SongId, keys: Set<MetaKey>)
//    fun removeAllExceptOfAllowedKeys(id: SongId, allowed: Set<MetaKey>)
    fun insertMeta(id: SongId, data: List<Pair<MetaKey, String>>): Long?
    fun clearMeta(id: SongId)
}