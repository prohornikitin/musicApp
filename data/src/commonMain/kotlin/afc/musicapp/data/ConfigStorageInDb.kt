package afc.musicapp.data

import afc.musicapp.data.impure.iface.db.DbQueryInterpreter
import afc.musicapp.domain.logic.impure.iface.storage.ConfigStorage
import afc.musicapp.data.pure.sql.mainDb.Config


class ConfigStorageInDb(private val db: DbQueryInterpreter) : ConfigStorage {
    override suspend fun get(key: String): String? {
        return db.executeOrDefault(Config.getValue(key))
    }

    override suspend fun set(key: String, value: String) {
        db.executeOrDefault(Config.setValue(key, value))
    }
}