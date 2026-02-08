package afc.musicapp.domain.logic.impure.impl

import afc.musicapp.domain.entities.ConfigParam
import afc.musicapp.domain.logic.impure.iface.storage.KvConfig
import afc.musicapp.domain.logic.impure.iface.storage.read.MetaFormatConfigRead
import afc.musicapp.domain.logic.impure.iface.storage.write.MetaFormatConfigUpdate

class MetaFormatConfigImpl(
    private val config: KvConfig,
): MetaFormatConfigRead, MetaFormatConfigUpdate {
    override suspend fun getDelimiter(): String {
        return config.get(ConfigParam.Companion.metaDelimiter)
    }

    override suspend fun setDelimiter(delimiter: String) {
        config.put(ConfigParam.Companion.metaDelimiter, delimiter)
    }
}