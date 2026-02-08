package afc.musicapp.domain.logic.impure.iface.storage.write

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SongId

interface MetadataEdit {
    suspend fun replaceMeta(id: SongId, data: List<Pair<MetaKey, String>>): SongId?
}