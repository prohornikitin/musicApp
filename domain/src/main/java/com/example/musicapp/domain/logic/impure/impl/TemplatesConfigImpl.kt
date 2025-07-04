package com.example.musicapp.domain.logic.impure.impl

import com.example.config.SongCardText
import com.example.musicapp.domain.data.ConfigKey
import com.example.musicapp.domain.logic.impure.iface.ReadConfig
import com.example.musicapp.domain.logic.impure.iface.storage.read.Config
import javax.inject.Inject


fun getTemplates(readConfig: ReadConfig): SongCardText {
    val map = readConfig(ConfigKey.MAIN_TEMPLATE)
    return SongCardText(
        readConfig(ConfigKey.MAIN_TEMPLATE) ?: "",
        readConfig(ConfigKey.SUB_TEMPLATE) ?: "",
    )
}