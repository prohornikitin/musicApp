package com.example.musicapp.domain.logic.impure.iface.storage.l2.write

import com.example.musicapp.domain.data.SongCardTemplates

interface TemplatesConfigUpdate {
    fun setTemplates(templates: SongCardTemplates)
}