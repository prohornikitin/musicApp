package com.example.musicapp.domain.logic.impure.impl.storage.l2

import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.ThumbnailStorageRead
import com.example.musicapp.domain.logic.impure.iface.storage.l1.write.ThumbnailStorageWrite
import com.example.musicapp.domain.logic.impure.iface.storage.l2.read.ObservableThumbnailStorage

class ObservableThumbnailStorageImpl(
    private val reader: ThumbnailStorageRead,
    private val writer: ThumbnailStorageWrite,
) : ObservableThumbnailStorage{
    override fun get(id: SongId): ByteArray? = reader.getIconFile(id)
    override val listeners: MutableCollection<ObservableThumbnailStorage.Listener> = mutableListOf()
}