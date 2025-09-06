package com.example.musicapp.domain.logic.impure.iface.storage.l1.write

import com.example.musicapp.domain.data.SongCardText
import com.example.musicapp.domain.data.SongId

interface SongCardTextUpdate {
    fun update(id: SongId, text: SongCardText)
}