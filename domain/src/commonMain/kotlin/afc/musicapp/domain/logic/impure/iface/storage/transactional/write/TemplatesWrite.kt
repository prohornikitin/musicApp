package afc.musicapp.domain.logic.impure.iface.storage.transactional.write

import afc.musicapp.domain.entities.SongCardTemplates
import afc.musicapp.domain.entities.SongCardText
import afc.musicapp.domain.entities.SongId

interface TemplatesWrite {
    suspend fun updateTemplates(templates: SongCardTemplates, songCardText: Map<SongId, SongCardText>)
}