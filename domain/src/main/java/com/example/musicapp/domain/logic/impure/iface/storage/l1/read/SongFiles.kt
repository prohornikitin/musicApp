package com.example.musicapp.domain.logic.impure.iface.storage.l1.read

import com.example.musicapp.domain.data.SongId
import java.io.File

interface SongFiles {
    fun getById(id: SongId): File?
    fun getAllByIds(ids: List<SongId>): Map<SongId, File>
}