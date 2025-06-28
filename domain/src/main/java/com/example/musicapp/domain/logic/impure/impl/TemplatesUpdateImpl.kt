package com.example.musicapp.domain.logic.impure.impl

import com.example.config.SongCardText
import com.example.musicapp.domain.data.Template
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.GeneratedTemplatesEdit
import com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed.TemplateUpdate
import com.example.musicapp.domain.logic.impure.iface.storage.read.MetaStorage
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.TemplatesConfigDbEdit
import com.example.musicapp.domain.logic.pure.applyTemplate
import javax.inject.Inject

class TemplatesUpdateImpl @Inject constructor(
    private val generatedTemplatesEdit: GeneratedTemplatesEdit,
    private val templatesConfigDbEdit: TemplatesConfigDbEdit,
    private val metaStorage: MetaStorage,
): TemplateUpdate {
    override fun updateTemplates(main: Template, sub: Template) {
        templatesConfigDbEdit.updateTemplates(main, sub)
        val usedKeys = main.getUsedKeys() + sub.getUsedKeys()
        val metas = metaStorage.getMetadataFields(usedKeys)
        generatedTemplatesEdit.updateStoredTemplates(metas.mapValues {
            SongCardText(applyTemplate(main, it.value), applyTemplate(sub, it.value))
        })
    }
}