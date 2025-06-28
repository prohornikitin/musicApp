package com.example.musicapp.domain.logic.impure.iface.storage.read

import com.example.config.SongCardText

fun interface TemplatesConfig {
    fun getTemplates(): SongCardText
}