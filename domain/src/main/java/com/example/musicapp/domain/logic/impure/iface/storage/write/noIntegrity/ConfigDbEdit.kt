package com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity

import com.example.musicapp.domain.data.ConfigKey

interface ConfigDbEdit {
    fun update(key: ConfigKey, value: String)
    fun update(values: Map<ConfigKey, String>)
}