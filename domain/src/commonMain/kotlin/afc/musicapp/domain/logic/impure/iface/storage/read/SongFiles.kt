package afc.musicapp.domain.logic.impure.iface.storage.read

import afc.musicapp.domain.entities.SongId
import okio.Path

interface SongFiles {
    suspend fun getById(id: SongId): Path?
    suspend fun getAllByIds(ids: List<SongId>): Map<SongId, Path>
}