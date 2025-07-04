package com.example.musicapp.domain.logic.impure.iface.storage.write

import com.example.musicapp.domain.data.SongId
import java.io.File

fun interface SongFileDbAdd {
    fun putNewIfNotExists(file: File): SongId?
}