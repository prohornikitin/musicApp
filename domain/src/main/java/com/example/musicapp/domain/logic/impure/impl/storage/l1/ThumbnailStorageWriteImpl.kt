package com.example.musicapp.domain.logic.impure.impl.storage.l1

import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.PlatformValues
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.ThumbnailStorageRead
import com.example.musicapp.domain.logic.impure.iface.storage.l1.write.ThumbnailStorageWrite
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import org.apache.commons.codec.digest.DigestUtils
import java.io.ByteArrayInputStream
import java.io.File

class ThumbnailStorageWriteImpl(
    private val platformValues: PlatformValues,
    private val db: DbQueryInterpreter
) : ThumbnailStorageWrite, ThumbnailStorageRead {
    override fun saveOrJustGetExistingRelativePath(data: ByteArray): String {
        return ByteArrayInputStream(data).use {
            val md5 = DigestUtils.md5Hex(it)
            val file = File(platformValues.iconsDir + "/" + md5)
            if (!file.exists()) {
                file.writeBytes(data)
            }
            md5
        }
    }

    override fun getIconFile(song: SongId): ByteArray? {
        return File(
            platformValues.iconsDir + db.execute(MainDbSetup.getIconPath(song))
        ).takeIf { it.exists() }?.readBytes()
    }
}