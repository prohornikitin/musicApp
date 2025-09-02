package com.example.musicapp.domain.logic.impure.iface

import com.example.musicapp.domain.data.SongId
import java.io.File

interface SongFileImport {
    fun importIfFileNotExists(file: File)
    fun reloadDataFromFile(file: File)
}