package afc.musicapp.domain.logic.impure.impl.storage

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import afc.musicapp.domain.logic.impure.iface.storage.MetaKeyMappingStorage
import afc.musicapp.domain.logic.pure.sql.mainDb.MetaMapping

class MetaKeyMappingStorageImpl(
    private val db: DbQueryInterpreter,
) : MetaKeyMappingStorage {
    override suspend fun getByKeyIfExists(key: String): MetaKey? {
        return db.executeOrDefault(MetaMapping.get(key))
    }

    override suspend fun addOrUpdateMapping(key: String, meta: MetaKey) {
        db.executeOrDefault(MetaMapping.put(key, meta))
    }
}