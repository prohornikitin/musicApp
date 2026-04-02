package afc.musicapp.domain.logic.impure.iface

import afc.musicapp.domain.entities.SongId

interface SongRemove {
    suspend fun remove(id: SongId)
}