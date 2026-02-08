package afc.musicapp.domain.logic.impure.iface

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SongId

interface MergedMetaRead {
    suspend fun getAllFieldsMerged(song: SongId): Map<MetaKey, String>
    suspend fun getAllFieldsMerged(songs: List<SongId>): Map<SongId, Map<MetaKey, String>>
    suspend fun getFieldsMerged(song: SongId, fields: Set<MetaKey>): Map<MetaKey, String>
    suspend fun getFieldsMerged(songs: List<SongId>, fields: Set<MetaKey>): Map<SongId, Map<MetaKey, String>>
}