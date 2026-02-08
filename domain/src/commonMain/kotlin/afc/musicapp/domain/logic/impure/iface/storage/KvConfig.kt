package afc.musicapp.domain.logic.impure.iface.storage

import afc.musicapp.domain.entities.ConfigParam
import afc.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter

interface KvConfig {
    suspend fun <T : Any> get(db: DbQueryInterpreter, param: ConfigParam<T>): T
    suspend fun <T : Any> put(db: DbQueryInterpreter, param: ConfigParam<T>, value: T)
}




