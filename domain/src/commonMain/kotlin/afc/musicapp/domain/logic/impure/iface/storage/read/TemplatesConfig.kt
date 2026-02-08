package afc.musicapp.domain.logic.impure.iface.storage.read

import afc.musicapp.domain.entities.SongCardTemplates

interface TemplatesConfig {
    suspend fun getTemplates(): SongCardTemplates
}