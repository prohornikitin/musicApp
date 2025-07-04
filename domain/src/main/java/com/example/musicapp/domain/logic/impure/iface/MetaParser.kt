package com.example.musicapp.domain.logic.impure.iface

import com.example.musicapp.domain.data.Metadata
import com.example.musicapp.domain.data.MetaKey
import java.io.File

interface MetaParser {
    fun getFullMetaFromFile(file: File): Metadata
}

typealias RetrieveMetaFromFile = (File, metaKeyMapping: Map<String, MetaKey>) -> Metadata