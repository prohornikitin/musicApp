package com.example.musicapp.domain.logic.impure.iface

import com.example.musicapp.domain.data.FullMeta
import com.example.musicapp.domain.data.MetaKey
import java.io.File

interface MetaParser {
    fun getFullMetaFromFile(file: File, tagsMapping: Map<String, MetaKey>): FullMeta
}