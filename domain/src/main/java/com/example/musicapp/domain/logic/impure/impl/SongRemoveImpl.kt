package com.example.musicapp.domain.logic.impure.impl

import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed.SongRemove
import com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed.SongThumbnailUpdate
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.MetaDbRemove
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.SongFileDbRemove
import javax.inject.Inject

class SongRemoveImpl @Inject constructor(
    private val fileRemove: SongFileDbRemove,
    private val metaRemove: MetaDbRemove,
    private val thumbnailStorageUpdate: SongThumbnailUpdate,
) : SongRemove {
    override fun remove(id: SongId) {
        thumbnailStorageUpdate.saveIcon(id, null)
        metaRemove.removeAllMeta(id)
        fileRemove.removeFile(id)
    }
}