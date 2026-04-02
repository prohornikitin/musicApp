package afc.musicapp.domain.logic.impure.iface.storage.read

import afc.musicapp.domain.entities.MetaKey

interface MetaKeyMappingRead {
    suspend fun getByKey(key: String): MetaKey = getByKeyIfExists(key) ?: MetaKey(key)
    suspend fun getByKeyIfExists(key: String): MetaKey?
}