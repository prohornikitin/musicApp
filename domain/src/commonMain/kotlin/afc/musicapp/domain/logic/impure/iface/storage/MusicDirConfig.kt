package afc.musicapp.domain.logic.impure.iface.storage

import okio.Path


interface MusicDirConfig {
    suspend fun getDir(): Path?
    suspend fun updateDir(directory: Path)
}