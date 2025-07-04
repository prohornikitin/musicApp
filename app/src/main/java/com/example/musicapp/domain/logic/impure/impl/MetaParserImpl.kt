package com.example.musicapp.domain.logic.impure.impl

import android.os.ParcelFileDescriptor
import com.example.musicapp.domain.data.Metadata
import com.example.musicapp.domain.logic.impure.iface.MetaParser
import com.example.musicapp.domain.logic.impure.iface.storage.v2.read.MetaKeyMappingStorage
import com.kyant.taglib.TagLib
import java.io.File
import javax.inject.Inject

class MetaParserImpl @Inject constructor(
    private val metaKeyMappingStorage: MetaKeyMappingStorage,
) : MetaParser {
    override fun getFullMetaFromFile(file: File): Metadata {
        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE).use { fd ->
            val meta = TagLib.getMetadata(fd.dup().detachFd())
            val properties = (meta?.propertyMap ?: emptyMap())
                .mapKeys { metaKeyMappingStorage.getByKey(it.key) }
                .mapValues { it.value.joinToString(",") }
            val icon = meta?.pictures?.firstOrNull()?.data
            return Metadata(properties, icon)
        }
    }
}