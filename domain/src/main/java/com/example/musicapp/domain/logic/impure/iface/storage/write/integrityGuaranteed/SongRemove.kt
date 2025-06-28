package com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed

import com.example.musicapp.domain.data.SongId

fun interface SongRemove {
    fun remove(id: SongId)
}