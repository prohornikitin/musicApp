package com.example.musicapp.domain.logic.impure.iface

import java.io.File

interface SongFileImport {
    fun importIfFileNotExists(file: File, deleteMetaFromFileAfterImport: Boolean = false)
    fun reloadDataFromFile(file: File)
}