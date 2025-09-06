package com.example.musicapp.domain.logic.impure.impl.storage.l1

import com.example.musicapp.domain.data.ConfigKey
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.KvConfigRead
import com.example.musicapp.domain.logic.impure.iface.storage.l1.write.KvConfigUpdate
import com.example.musicapp.domain.logic.pure.sql.mainDb.Config

class KvConfigImpl(
    private val db: DbQueryInterpreter
) : KvConfigRead, KvConfigUpdate {
    override fun get(key: ConfigKey): String? = db.execute(Config.getValue(key))
    override fun update(key: ConfigKey, value: String) = db.execute(Config.setValue(key, value))
}