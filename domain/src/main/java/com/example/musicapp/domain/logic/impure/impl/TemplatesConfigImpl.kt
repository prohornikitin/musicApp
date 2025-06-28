package com.example.musicapp.domain.logic.impure.impl

import com.example.config.SongCardText
import com.example.musicapp.domain.data.ConfigKey
import com.example.musicapp.domain.logic.impure.iface.storage.read.Config
import com.example.musicapp.domain.logic.impure.iface.storage.read.TemplatesConfig
import javax.inject.Inject

class TemplatesConfigImpl @Inject constructor(
    val config: Config,
) : TemplatesConfig {
    override fun getTemplates(): SongCardText {
        val map = config.read(listOf(ConfigKey.MAIN_TEMPLATE, ConfigKey.SUB_TEMPLATE))
        return SongCardText(
            map[ConfigKey.MAIN_TEMPLATE]!!,
            map[ConfigKey.SUB_TEMPLATE]!!,
        )
    }
}