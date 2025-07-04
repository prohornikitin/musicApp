package com.example.musicapp.domain.logic.impure.iface

import com.example.musicapp.domain.data.SongId
import java.io.File

interface SongFileLoad {
    fun loadNewIfNotExists(file: File)
    fun loadNewOrUpdateData(file: File)
    fun reloadDataFromFile(id: SongId)
}