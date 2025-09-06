package com.example.musicapp.domain.logic.impure.impl.storage.l2

import com.example.musicapp.domain.data.SongCardText
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.SongCardTextStorage
import com.example.musicapp.domain.logic.impure.iface.storage.l2.read.ObservableSongCardTextStorage
import com.example.musicapp.domain.logic.impure.iface.storage.l2.read.ObservableSongCardTextStorage.Listener

class ObservableSongCardTextStorageImpl(
    private val storage: SongCardTextStorage,
) : ObservableSongCardTextStorage {
    override fun get(id: SongId): SongCardText {
        return storage.get(id)!!
    }

    override val listeners: MutableCollection<Listener> = mutableListOf()
}