package afc.musicapp.data.impure.impl

import afc.musicapp.data.impure.iface.ConfigCache
import afc.musicapp.domain.entities.ConfigParam
import afc.musicapp.domain.logic.impure.iface.MemoryCache
import io.ktor.util.collections.ConcurrentMap

class ConfigCacheImpl : ConfigCache {
    private val cache: ConcurrentMap<String, Any> = ConcurrentMap()

    init {
        MemoryCache.add { cache.clear() }
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : Any> get(param: ConfigParam<T>): T? = cache[param.key] as T?

    override suspend fun <T : Any> put(param: ConfigParam<T>, value: T) {
        cache[param.key] = value
    }
}