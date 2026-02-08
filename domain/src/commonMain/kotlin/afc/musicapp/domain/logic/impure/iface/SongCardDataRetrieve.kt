package afc.musicapp.domain.logic.impure.iface

import afc.musicapp.domain.entities.SongCardData
import afc.musicapp.domain.entities.SongId

interface SongCardDataRetrieve {
    suspend fun get(id: SongId): SongCardData?
}