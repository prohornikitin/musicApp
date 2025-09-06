package com.example.musicapp.domain.logic.impure.impl.storage.l2

import com.example.musicapp.domain.data.ConfigKey
import com.example.musicapp.domain.data.SongCardTemplates
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.KvConfigRead
import com.example.musicapp.domain.logic.impure.iface.storage.l1.write.KvConfigUpdate
import com.example.musicapp.domain.logic.impure.iface.storage.l2.read.TemplatesConfigRead
import com.example.musicapp.domain.logic.impure.iface.storage.l2.write.TemplatesConfigUpdate
import com.example.musicapp.domain.logic.pure.parseTemplate
import javax.inject.Inject

class TemplatesConfigImpl @Inject constructor(
    private val configRead: KvConfigRead,
    private val configUpdate: KvConfigUpdate,
): TemplatesConfigRead, TemplatesConfigUpdate {
    private fun getTemplate(key: ConfigKey) =
        parseTemplate(configRead.get(key) ?: "")

    override fun getTemplates() = SongCardTemplates(
        getTemplate(ConfigKey.MAIN_TEMPLATE),
        getTemplate(ConfigKey.SUB_TEMPLATE),
    )

    override val listeners: MutableCollection<TemplatesConfigRead.ChangeListener> = mutableListOf()

    override fun setTemplates(templates: SongCardTemplates) {
        configUpdate.run {
            update(ConfigKey.MAIN_TEMPLATE, templates.main.toString())
            update(ConfigKey.SUB_TEMPLATE, templates.bottom.toString())
        }
        listeners.forEach { it.onTemplatesChange(templates) }
    }
}