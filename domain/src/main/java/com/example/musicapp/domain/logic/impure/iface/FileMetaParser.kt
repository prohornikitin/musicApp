package com.example.musicapp.domain.logic.impure.iface

import com.example.musicapp.domain.data.Metadata
import java.io.File

interface FileMetaParser {
    fun getFullMetaFromFile(file: File): Metadata
}