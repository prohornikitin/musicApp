package com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed

import com.example.musicapp.domain.data.Template

fun interface TemplateUpdate {
    fun updateTemplates(main: Template, sub: Template)
}