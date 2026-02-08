package afc.musicapp.domain.logic.impure.impl

import afc.musicapp.domain.entities.ConfigParam
import afc.musicapp.domain.entities.SongCardTemplates
import afc.musicapp.domain.logic.impure.iface.storage.KvConfig
import afc.musicapp.domain.logic.impure.iface.storage.read.TemplatesRead

class TemplatesReadImpl(
    private val config: KvConfig,
): TemplatesRead {
    override suspend fun getTemplates() = SongCardTemplates(
        config.get(ConfigParam.Companion.mainTemplate),
        config.get(ConfigParam.Companion.subTemplate),
    )

    override suspend fun setTemplates(templates: SongCardTemplates) {
        config.run {
            put(ConfigParam.Companion.mainTemplate, templates.main)
            put(ConfigParam.Companion.subTemplate, templates.bottom)
        }
    }
}