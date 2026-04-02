package afc.musicapp.data.db

import afc.musicapp.data.LowLevelConfigImpl
import afc.musicapp.data.impure.impl.ConfigCacheImpl
import afc.musicapp.domain.entities.ConfigParam
import afc.musicapp.domain.entities.SongCardTemplates
import afc.musicapp.domain.logic.impure.iface.MemoryCache
import afc.musicapp.domain.logic.impure.iface.storage.ConfigStorage
import afc.musicapp.domain.logic.pure.parseTemplate
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class LLConfigTest {

    private fun createMockStorage(initialState: Map<String, String> = emptyMap()): ConfigStorage {
        return object : ConfigStorage {
            private val kvMap = initialState.toMutableMap()

            override suspend fun get(key: String): String? = kvMap[key]
            override suspend fun set(key: String, value: String) {
                kvMap.put(key, value)
            }
        }
    }

    fun createConfigImpl() = LowLevelConfigImpl(ConfigCacheImpl(), logger = createMockLogger())

//    private fun createLLConfig():

    @Test
    fun templates_default() = runTest {
        assertEquals(
            ConfigParam.Companion.templates.default,
            createConfigImpl().get(createMockStorage(), ConfigParam.Companion.templates),
        )
    }

    private suspend fun <T : Any> testSetGet(param: ConfigParam<T>, data: T) {
        val storage = createMockStorage()
        val config = createConfigImpl()
        config.put(storage, param, data)
        MemoryCache.Companion.cleanupAll()
        assertEquals(data, config.get(storage, param))
    }

    @Test
    fun setGet_templates() = runTest {
        testSetGet(
            ConfigParam.Companion.templates,
            SongCardTemplates(
                parseTemplate("#A"),
                parseTemplate("#B"),
            )
        )
    }

    @Test
    fun templates_parseError() = runTest {
        val storage = createMockStorage(
            mapOf(
                ConfigParam.Companion.templates.key to "{}"
            )
        )
        assertEquals(
            ConfigParam.Companion.templates.default,
            createConfigImpl().get(storage, ConfigParam.Companion.templates),
        )

    }
}