package afc.musicapp.data.impure.iface

import afc.musicapp.domain.entities.ConfigParam

interface ConfigCache {
    suspend fun <T : Any> get(param: ConfigParam<T>): T?
    suspend fun <T : Any> put(param: ConfigParam<T>, value: T)
}