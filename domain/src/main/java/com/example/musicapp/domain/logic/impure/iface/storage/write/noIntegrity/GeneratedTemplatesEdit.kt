package com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity

import com.example.config.SongCardText
import com.example.musicapp.domain.data.SongId

interface GeneratedTemplatesEdit {
    fun updateStoredTemplates(templates: Map<SongId, SongCardText>)
}