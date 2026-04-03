package afc.musicapp.data

import afc.musicapp.data.impure.iface.LowLevelConfig
import afc.musicapp.data.impure.iface.ConfigCache
import afc.musicapp.domain.entities.ConfigParam
import afc.musicapp.domain.logic.impure.iface.storage.ConfigStorage
import afc.musicapp.domain.logic.impure.impl.logger.Logger
import afc.musicapp.domain.logic.pure.logger.withClassTag
import kotlinx.coroutines.CancellationException

class LowLevelConfigImpl constructor(
    private val cache: ConfigCache,
    logger: Logger,
) : LowLevelConfig {
    private val logger = logger.withClassTag<LowLevelConfig>()

    override suspend fun <T : Any> put(storage: ConfigStorage, param: ConfigParam<T>, value: T) {
        cache.put(param, value)
        storage.set(
            param.key,
            param.codec.encode(value)
        )
    }

    @Suppress("UNCHECKED_CAST")
    override suspend fun <T : Any> get(storage: ConfigStorage, param: ConfigParam<T>): T {
        val cached = cache.get(param)
        if (cached != null) {
            return cached
        }
        val value = try {
            getAndParse(storage, param)
        } catch (e: CancellationException) {
            throw e
        } catch (e: Exception) {
            logger.error(e)
            param.default
        }
        cache.put(param, value)
        return value
    }

    private suspend fun <T : Any> getAndParse(storage: ConfigStorage, param: ConfigParam<T>): T {
        val strValue = storage.get(param.key)
        return strValue?.let {
            param.codec.decode(strValue)
        } ?: param.default
    }
}