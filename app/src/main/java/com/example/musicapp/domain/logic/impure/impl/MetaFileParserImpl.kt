package com.example.musicapp.domain.logic.impure.impl

import android.os.ParcelFileDescriptor
import androidx.media3.common.MimeTypes
import com.example.musicapp.domain.data.Metadata
import com.example.musicapp.domain.logic.impure.iface.FileMetaUpdate
import com.example.musicapp.domain.logic.impure.iface.FileMetaParser
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.MetaKeyMappingStorage
import com.kyant.taglib.Picture
import com.kyant.taglib.PropertyMap
import com.kyant.taglib.TagLib
import java.io.File
import javax.inject.Inject

class MetaFileParserImpl @Inject constructor(
    private val metaKeyMappingStorage: MetaKeyMappingStorage,
) : FileMetaParser, FileMetaUpdate {
    override fun getFullMetaFromFile(file: File): Metadata {
        return ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_ONLY).use { fd ->
            val meta = TagLib.getMetadata(fd.dup().detachFd())
            val properties = (meta?.propertyMap ?: emptyMap())
                .mapKeys { metaKeyMappingStorage.getByKey(it.key) }
                .mapValues { it.value.joinToString(",") }
            val icon = meta?.pictures?.firstOrNull()?.data
            Metadata(properties, icon)
        }
    }

    override fun clearMeta(file: File) {
        ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE).use { fd ->
            TagLib.savePictures(fd.dup().detachFd(), emptyArray())
            TagLib.savePropertyMap(fd.dup().detachFd(), PropertyMap())
        }
    }

    override fun updateMeta(file: File, meta: Metadata) {
        ParcelFileDescriptor.open(file, ParcelFileDescriptor.MODE_READ_WRITE).use { fd ->
            val fd2 = fd.dup().detachFd()
            TagLib.savePropertyMap(fd2, PropertyMap())
            if (meta.icon != null) {
                TagLib.savePictures(fd2, arrayOf(Picture(meta.icon!!, "", "", "image")))
            }
        }
    }
}