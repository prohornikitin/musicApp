package com.example.musicapp.domain.logic.impure.iface.storage.l1.read

import com.example.musicapp.domain.data.MetaKey

interface MetaKeyMappingStorage {
    fun getByKey(key: String): MetaKey = getByKeyIfExists(key) ?: MetaKey(key)
    fun getByKeyIfExists(key: String): MetaKey?
}