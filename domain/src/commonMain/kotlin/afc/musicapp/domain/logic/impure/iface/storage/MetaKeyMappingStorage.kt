package afc.musicapp.domain.logic.impure.iface.storage

import afc.musicapp.domain.entities.MetaKey

interface MetaKeyMappingStorage {
    suspend fun getByKey(key: String): MetaKey = getByKeyIfExists(key) ?: MetaKey(key)
    suspend fun getByKeyIfExists(key: String): MetaKey?
    suspend fun addOrUpdateMapping(key: String, meta: MetaKey)
}