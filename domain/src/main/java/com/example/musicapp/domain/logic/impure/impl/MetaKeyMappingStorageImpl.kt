package com.example.musicapp.domain.logic.impure.impl

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.impure.iface.storage.v2.read.MetaKeyMappingStorage
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSql

class MetaKeyMappingStorageImpl(
    private val db: DbQueryInterpreter
) : MetaKeyMappingStorage {
    override fun getByKeyIfExists(key: String): MetaKey? {
        return db.execute(MainDbSql.getMetaMappingByKey(key))
    }
}