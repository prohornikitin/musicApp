package afc.musicapp.domain.logic.impure.impl

import afc.musicapp.domain.entities.ConfigParam
import afc.musicapp.domain.logic.impure.iface.storage.KvConfig
import okio.Path

class MusicDirsConfigImpl(
    val kvConfig: KvConfig,
) : MusicDirsConfig {
    override suspend fun getDir(): Path? {
        return kvConfig.get(ConfigParam.Companion.musicFolders).getOrNull()
    }
}