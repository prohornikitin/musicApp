package afc.musicapp.domain.logic.impure.iface

import afc.musicapp.domain.entities.Metadata
import okio.Path

interface FileMetaParser {
    suspend fun getFullMetaFromFile(path: Path): Metadata
}