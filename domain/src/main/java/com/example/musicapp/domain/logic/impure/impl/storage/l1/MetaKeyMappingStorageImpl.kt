package com.example.musicapp.domain.logic.impure.impl.storage.l1

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.MetaKeyMappingStorage
import com.example.musicapp.domain.logic.pure.sql.mainDb.MetaMapping
import javax.inject.Inject

class MetaKeyMappingStorageImpl @Inject constructor(
    private val db: DbQueryInterpreter
) : MetaKeyMappingStorage {
    override fun getByKeyIfExists(key: String): MetaKey? {
        return db.execute(MetaMapping.get(key))
    }
}