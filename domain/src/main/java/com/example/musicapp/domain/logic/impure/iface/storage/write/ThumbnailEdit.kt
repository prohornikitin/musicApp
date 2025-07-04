package com.example.musicapp.domain.logic.impure.iface.storage.write

import com.example.musicapp.domain.data.SongId

interface ThumbnailEdit {
    fun update(id: SongId, data: ByteArray?)
}