package afc.musicapp.data

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.data.impure.iface.db.DbQueryInterpreter
import afc.musicapp.domain.logic.impure.iface.storage.read.MetaKeyMappingRead
import afc.musicapp.domain.logic.impure.iface.storage.write.MetaKeyMappingEdit
import afc.musicapp.data.pure.sql.mainDb.MetaMapping

class MetaKeyMappingStorageImpl(
    private val db: DbQueryInterpreter,
) : MetaKeyMappingRead, MetaKeyMappingEdit {
    override suspend fun getByKeyIfExists(key: String): MetaKey? {
        return db.executeOrDefault(MetaMapping.get(key))
    }

    override suspend fun addOrUpdateMapping(key: String, meta: MetaKey) {
        db.executeOrDefault(MetaMapping.put(key, meta))
    }
}