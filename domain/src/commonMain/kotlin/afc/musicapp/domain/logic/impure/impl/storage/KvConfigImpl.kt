package afc.musicapp.domain.logic.impure.impl.storage

import afc.musicapp.domain.entities.ConfigParam
import afc.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import afc.musicapp.domain.logic.impure.iface.storage.KvConfig
import afc.musicapp.domain.logic.pure.sql.mainDb.Config
import com.mayakapps.rwmutex.withWriteLock
import io.ktor.util.collections.ConcurrentMap

class KvConfigImpl : KvConfig {
    private val cache: ConcurrentMap<String, Any> = ConcurrentMap()

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : Any> get(db: DbQueryInterpreter, param: ConfigParam<T>): T {
        val key = param.key
        if (!cache.containsKey(key)) {
            val value = db.executeOrDefault(Config.getValue(key))
                ?.let(param.codec::decode)
                ?: param.default
            cache[key] = value
            return value
        } else {
            return cache[key] as T
        }
    }

    override suspend fun <T : Any> put(db: DbQueryInterpreter, param: ConfigParam<T>, value: T) {
        cache[param.key] = value
        db.executeOrDefault(
            Config.setValue(
            param.key,
            param.codec.encode(value)
        ))
    }
}