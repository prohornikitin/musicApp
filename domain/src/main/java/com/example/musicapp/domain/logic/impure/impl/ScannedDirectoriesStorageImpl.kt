package com.example.musicapp.domain.logic.impure.impl

import com.example.musicapp.domain.data.ConfigKey
import com.example.musicapp.domain.logic.impure.iface.storage.read.Config
import com.example.musicapp.domain.logic.impure.iface.storage.read.ScannedDirectoriesStorage
import kotlinx.serialization.json.Json
import javax.inject.Inject

class ScannedDirectoriesStorageImpl @Inject constructor(
    val config: Config,
) : ScannedDirectoriesStorage {
    override fun getDirectories(): List<String> {
        val str = config.read(ConfigKey.SCAN_DIRECTORIES)
        if (str == null) {
            return emptyList()
        }
        return Json.decodeFromString<List<String>>(str)
    }
}