package com.example.musicapp.domain.logic.impure.iface

import com.example.musicapp.domain.data.Metadata
import com.example.musicapp.domain.data.MetaKey
import java.io.File

interface FileMetaUpdate {
    fun updateMeta(file: File, meta: Metadata)
    fun clearMeta(file: File)
}