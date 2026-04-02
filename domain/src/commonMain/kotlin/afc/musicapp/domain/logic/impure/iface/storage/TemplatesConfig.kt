package afc.musicapp.domain.logic.impure.iface.storage

import afc.musicapp.domain.entities.SongCardTemplates

interface TemplatesConfig {
    suspend fun getTemplates(): SongCardTemplates
    suspend fun updateTemplates(new: SongCardTemplates)
}