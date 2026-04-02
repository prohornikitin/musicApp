package afc.musicapp.data

import afc.musicapp.domain.entities.Metadata
import afc.musicapp.domain.logic.impure.iface.Dispatchers
import afc.musicapp.domain.logic.impure.iface.FileMetaParser
import afc.musicapp.domain.logic.impure.iface.FileMetaUpdate
import afc.musicapp.domain.logic.impure.iface.storage.read.MetaKeyMappingRead
import android.os.ParcelFileDescriptor
import com.kyant.taglib.Picture
import com.kyant.taglib.TagLib
import kotlinx.coroutines.withContext
import okio.Path

class MetaFileParserImpl constructor(
    private val metaKeyMappingStorage: MetaKeyMappingRead,
    private val dispatchers: Dispatchers,
) : FileMetaParser, FileMetaUpdate {
    override suspend fun getFullMetaFromFile(path: Path): Metadata = withContext(dispatchers.io) {
        ParcelFileDescriptor.open(path.toFile(), ParcelFileDescriptor.MODE_READ_ONLY).use { fd ->
            val meta = TagLib.getMetadata(fd.dup().detachFd())
            val properties = (meta?.propertyMap ?: emptyMap())
                .mapKeys { metaKeyMappingStorage.getByKey(it.key) }
                .mapValues { it.value.joinToString(",") }
            val icon = meta?.pictures?.firstOrNull()?.data
            Metadata(properties, icon)
        }
    }

    override suspend fun clearMeta(path: Path): Unit = withContext(dispatchers.io) {
        ParcelFileDescriptor.open(path.toFile(), ParcelFileDescriptor.MODE_READ_WRITE).use { fd ->
            TagLib.savePictures(fd.dup().detachFd(), emptyArray())
            TagLib.savePropertyMap(fd.dup().detachFd(), HashMap())
        }
    }

    override suspend fun updateMeta(path: Path, meta: Metadata) = withContext(dispatchers.io) {
        ParcelFileDescriptor.open(path.toFile(), ParcelFileDescriptor.MODE_READ_WRITE).use { fd ->
            val fd2 = fd.dup().detachFd()
            TagLib.savePropertyMap(fd2, HashMap())
            if (meta.icon != null) {
                TagLib.savePictures(fd2, arrayOf(Picture(meta.icon!!, "", "", "image")))
            }
        }
    }
}