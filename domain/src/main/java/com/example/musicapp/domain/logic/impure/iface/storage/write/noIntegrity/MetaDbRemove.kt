package com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongId

interface MetaDbRemove {
    fun removeAllMeta(id: SongId)
    fun remove(id: SongId, keys: List<MetaKey>)
}