package com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed

import com.example.musicapp.domain.data.SongId
import java.io.File

fun interface SongAdd {
    fun addNewIfNotExists(file: File): SongId?
}