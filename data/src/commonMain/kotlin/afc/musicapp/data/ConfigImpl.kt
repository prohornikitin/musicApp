package afc.musicapp.data

import afc.musicapp.domain.entities.ConfigParam
import afc.musicapp.data.impure.iface.db.DbQueryInterpreter
import afc.musicapp.domain.logic.impure.iface.storage.MusicDirConfig
import afc.musicapp.domain.logic.impure.iface.storage.read.MetaFormatConfigRead
import afc.musicapp.domain.logic.pure.Optional
import afc.musicapp.data.impure.iface.LowLevelConfig
import okio.Path

class ConfigImpl constructor(
    db: DbQueryInterpreter,
    private val lowLevelConfig: LowLevelConfig,
) : MusicDirConfig, MetaFormatConfigRead {
    val storage = ConfigStorageInDb(db)
    
    override suspend fun getDir(): Path? {
        return lowLevelConfig.get(storage, ConfigParam.musicFolder).getOrNull()
    }

    override suspend fun updateDir(directory: Path) {
        return lowLevelConfig.put(storage, ConfigParam.musicFolder, Optional.some(directory))
    }

    override suspend fun getDelimiter(): String {
        return lowLevelConfig.get(storage, ConfigParam.metaDelimiter)
    }
}