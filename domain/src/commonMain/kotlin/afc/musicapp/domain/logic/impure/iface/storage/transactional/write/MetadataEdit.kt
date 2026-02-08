package afc.musicapp.domain.logic.impure.iface.storage.transactional.write

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SongCardText
import afc.musicapp.domain.entities.SongId

interface MetadataEdit {
    suspend fun replaceAllTagsForSong(song: SongId, tags: List<Pair<MetaKey, String>>)
    suspend fun updateAllTagsForSongAndUpdateText(song: SongId, tags: List<Pair<MetaKey, String>>, newText: SongCardText)
}