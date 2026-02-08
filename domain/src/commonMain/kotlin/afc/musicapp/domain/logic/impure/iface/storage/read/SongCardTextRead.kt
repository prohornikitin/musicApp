package afc.musicapp.domain.logic.impure.iface.storage.read

import afc.musicapp.domain.entities.SongCardText
import afc.musicapp.domain.entities.SongId

interface SongCardTextRead {
    suspend fun get(ids: List<SongId>): Map<SongId, SongCardText>
    suspend fun get(id: SongId): SongCardText? {
        return get(listOf(id))[id]
    }
}