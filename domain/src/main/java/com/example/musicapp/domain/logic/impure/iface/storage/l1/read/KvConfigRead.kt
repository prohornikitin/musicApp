package com.example.musicapp.domain.logic.impure.iface.storage.l1.read

import com.example.musicapp.domain.data.ConfigKey

interface KvConfigRead {
    fun get(key: ConfigKey): String?
}