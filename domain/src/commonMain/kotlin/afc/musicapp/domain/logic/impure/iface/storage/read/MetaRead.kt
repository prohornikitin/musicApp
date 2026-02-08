package afc.musicapp.domain.logic.impure.iface.storage.read

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SongId

interface MetaRead {
    suspend fun getAllFields(song: SongId): List<Pair<MetaKey, String>>
    suspend fun getFields(song: SongId, fields: Set<MetaKey>): List<Pair<MetaKey, String>>
    suspend fun getFields(songs: List<SongId>, fields: Set<MetaKey>): List<Triple<SongId, MetaKey, String>>
}