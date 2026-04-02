package afc.musicapp.data.impure.iface

import afc.musicapp.domain.entities.ConfigParam
import afc.musicapp.domain.logic.impure.iface.storage.ConfigStorage

interface LowLevelConfig {
    suspend fun <T : Any> get(storage: ConfigStorage, param: ConfigParam<T>): T
    suspend fun <T : Any> put(storage: ConfigStorage, param: ConfigParam<T>, value: T)
}