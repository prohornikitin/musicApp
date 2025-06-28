package com.example.musicapp.domain.logic.impure.impl

import com.example.musicapp.domain.data.ConfigKey
import com.example.musicapp.domain.data.Template
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.ConfigDbEdit
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.TemplatesConfigDbEdit
import javax.inject.Inject

class TemplatesConfigDbEditImpl @Inject constructor(
    val config: ConfigDbEdit,
) : TemplatesConfigDbEdit {
    override fun updateTemplates(main: Template, sub: Template) {
        config.update(mapOf(
            ConfigKey.MAIN_TEMPLATE to main.toString(),
            ConfigKey.SUB_TEMPLATE to sub.toString(),
        ))
    }
}