package afc.musicapp.domain.logic.impure.iface

import afc.musicapp.domain.entities.Metadata
import okio.Path

interface FileMetaUpdate {
    suspend fun updateMeta(path: Path, meta: Metadata)
    suspend fun clearMeta(path: Path)
}