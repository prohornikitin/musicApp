package com.example.musicapp.domain.logic.impure.impl

import android.os.ParcelFileDescriptor
import com.example.musicapp.domain.data.FullMeta
import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.logic.impure.iface.MetaParser
import com.kyant.taglib.TagLib
import java.io.File
import javax.inject.Inject

class MetaParserImpl @Inject constructor() : MetaParser {
    override fun getFullMetaFromFile(file: File, tagsMapping: Map<String, MetaKey>): FullMeta {
        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE).use { fd ->
            val meta = TagLib.getMetadata(fd.dup().detachFd())
            val properties = (meta?.propertyMap?.toMap() ?: emptyMap()).mapKeys {
                tagsMapping[it.key] ?: MetaKey(it.key)
            }.mapValues {
                it.value.joinToString(",")
            }
            val icon = meta?.pictures?.firstOrNull()?.data
            return FullMeta(
                properties,
                icon,
            )
        }
    }
}