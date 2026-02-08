package afc.musicapp.domain.logic.impure.iface.storage.write

import afc.musicapp.domain.entities.SongCardTemplates

interface TemplatesUpdate {
    suspend fun setTemplates(templates: SongCardTemplates)
}