package afc.musicapp.domain.logic.impure.iface.storage

import afc.musicapp.domain.entities.SongId

interface ThumbnailStorage {
    suspend fun getRawData(song: SongId): ByteArray?
//    suspend fun getPath(song: SongId): Path?
//    suspend fun saveOrJustGetExistingRelativePath(data: ByteArray): String
//    suspend fun linkSongWithIcon(song: SongId, relativePath: String)
    suspend fun updateIcon(song: SongId, data: ByteArray?)
    suspend fun deleteSongIcon(song: SongId)
}