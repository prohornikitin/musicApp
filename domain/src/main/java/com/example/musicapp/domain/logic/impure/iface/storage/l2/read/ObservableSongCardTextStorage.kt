package com.example.musicapp.domain.logic.impure.iface.storage.l2.read

import com.example.musicapp.domain.data.SongCardText
import com.example.musicapp.domain.data.SongId

interface ObservableSongCardTextStorage {
    fun interface Listener {
        fun onCardDataChanged(id: SongId, text: SongCardText)
    }

    val listeners: MutableCollection<Listener>
    fun get(id: SongId): SongCardText
}