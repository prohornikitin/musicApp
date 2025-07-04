package com.example.musicapp.domain.logic.impure.iface.storage.v2.read

import com.example.config.SongCardText
import com.example.musicapp.domain.data.SongId

interface SongCardTextStorage :
    ObservableStorage<SongCardText, SongCardTextStorage.ChangeCallback> {
    fun interface ChangeCallback {
        fun onCardChange(id: SongId, card: SongCardText)
    }
}