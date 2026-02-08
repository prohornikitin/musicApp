package afc.musicapp.domain.logic.impure.iface.storage.read

import afc.musicapp.domain.entities.SongCardTemplates

interface TemplatesRead {
    suspend fun getTemplates(): SongCardTemplates
}