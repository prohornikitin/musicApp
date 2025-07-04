package com.example.musicapp.domain.logic.impure.iface.storage.write

import com.example.musicapp.domain.data.ConfigKey

interface ConfigUpdate {
    fun update(key: ConfigKey, value: String) = update(mapOf(key to value))
    fun update(values: Map<ConfigKey, String>)
}