package afc.musicapp.domain.logic.impure.iface.storage.read

import afc.musicapp.domain.entities.SongCardData
import afc.musicapp.domain.entities.SongId

interface SongCardDataRead {
    suspend fun get(id: SongId): SongCardData?
}