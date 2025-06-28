package com.example.musicapp.domain.logic.impure.iface.storage.read

import com.example.musicapp.domain.data.SongId
import java.io.File

interface SongFileStorage {
    fun getFile(id: SongId): File?
    fun getFiles(ids: List<SongId>): Map<SongId, File>
}