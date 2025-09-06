package com.example.musicapp.domain.logic.impure.iface.storage.l1.write

import com.example.musicapp.domain.data.ConfigKey

interface KvConfigUpdate {
    fun update(key: ConfigKey, value: String)
}