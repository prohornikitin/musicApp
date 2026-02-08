package afc.musicapp.domain.logic.impure.iface

import okio.Path

interface SongFileImport {
    suspend fun importIfSongNotExistsAlready(file: Path, deleteMetaFromFileAfterImport: Boolean = false)
    suspend fun reloadDataFromFile(file: Path)
}