package afc.musicapp.domain.logic.impure.impl

import afc.musicapp.domain.entities.SongCardData
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.logic.impure.iface.SongCardDataRetrieve
import afc.musicapp.domain.logic.impure.iface.storage.ThumbnailStorage
import afc.musicapp.domain.logic.impure.iface.storage.read.SongCardTextRead

class SongCardDataRepoImpl(
    private val textStorage: SongCardTextRead,
    private val thumbnailStorage: ThumbnailStorage,
) : SongCardDataRetrieve {
    override suspend fun get(id: SongId): SongCardData? {
        val text = textStorage.get(id) ?: return null
        val icon = thumbnailStorage.getRawData(id)
        return SongCardData(
            text = text,
            icon = icon,
        )
    }
}