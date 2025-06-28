package com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity

import com.example.musicapp.domain.data.Template

fun interface TemplatesConfigDbEdit {
    fun updateTemplates(main: Template, sub: Template)
}