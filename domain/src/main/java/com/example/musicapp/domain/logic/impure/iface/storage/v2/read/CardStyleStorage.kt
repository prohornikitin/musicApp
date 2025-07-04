package com.example.musicapp.domain.logic.impure.iface.storage.v2.read

import com.example.config.SongCardStyle

interface CardStyleStorage : ObservableStorage<SongCardStyle, CardStyleStorage.ChangeCallback> {
    fun interface ChangeCallback {
        fun onStyleChange(style: SongCardStyle)
    }
}