package afc.musicapp.domain.logic.impure.iface.storage.write

import afc.musicapp.domain.entities.MetaKey

interface MetaKeyMappingEdit {
    suspend fun addOrUpdateMapping(key: String, meta: MetaKey)
}