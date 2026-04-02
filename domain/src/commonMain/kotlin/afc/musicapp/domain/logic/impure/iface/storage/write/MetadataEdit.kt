package afc.musicapp.domain.logic.impure.iface.storage.write

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SongId

interface MetadataEdit {
    suspend fun replaceAllTagsForSong(song: SongId, tags: List<Pair<MetaKey, String>>)
}