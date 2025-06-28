package com.example.musicapp.domain.logic.impure.iface.storage.read
import com.example.musicapp.domain.data.ConfigKey


interface Config {
    fun read(key: ConfigKey): String?
    fun read(keys: List<ConfigKey>): Map<ConfigKey, String>
}