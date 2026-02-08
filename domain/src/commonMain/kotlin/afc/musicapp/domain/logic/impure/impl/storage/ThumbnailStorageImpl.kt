package afc.musicapp.domain.logic.impure.impl.storage

import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.logic.impure.iface.Dispatchers
import afc.musicapp.domain.logic.impure.iface.PlatformValues
import afc.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import afc.musicapp.domain.logic.impure.iface.storage.ThumbnailStorage
import afc.musicapp.domain.logic.impure.impl.logger.Logger
import afc.musicapp.domain.logic.pure.Optional
import afc.musicapp.domain.logic.pure.logger.withClassTag
import afc.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import io.ktor.util.collections.ConcurrentMap
import kotlinx.coroutines.withContext
import okio.FileSystem
import okio.Path
import okio.Path.Companion.toPath
import org.kotlincrypto.hash.md.MD5

class ThumbnailStorageImpl(
    private val platformValues: PlatformValues,
    private val db: DbQueryInterpreter,
    private val dispatchers: Dispatchers,
    private val fileSystem: FileSystem,
    rawLogger: Logger
) : ThumbnailStorage {
    private val cachedPaths: ConcurrentMap<SongId, Optional<Path>> = ConcurrentMap()
    private val cachedData: ConcurrentMap<Path, ByteArray> = ConcurrentMap()
    private val logger = rawLogger.withClassTag(this)


    override suspend fun getRawData(song: SongId): ByteArray? = withContext(dispatchers.io) {
        val path = cachedPaths.getOrPut(song) {
            Optional.fromNullable(db.executeOrDefault(MainDbSetup.getIconPath(song))?.toPath())
        }.getOrNull() ?: return@withContext null
        val data = cachedData.getOrPut(path) {
            if (!fileSystem.exists(path)) {
                return@withContext null
            }
            fileSystem.read(path) {
                readByteArray()
            }
        }
        data
    }

    override suspend fun deleteSongIcon(song: SongId) {
        cachedPaths[song] = Optional.none()
        db.executeOrDefault(MainDbSetup.updateSongIconPath(song, null))
    }

    override suspend fun updateIcon(song: SongId, data: ByteArray?) {
        if (data == null) {
            return deleteSongIcon(song)
        }
        val relativePath = saveOrJustGetExistingRelativePath(data)
        linkSongWithIcon(song, relativePath)
        cachedData[relativePath] = data
        cachedPaths[song] = Optional.some(relativePath)
    }

    private fun saveOrJustGetExistingRelativePath(data: ByteArray): Path {
        val md5 = MD5().digest(data).toHexString()
        val filePath = (platformValues.iconsDirectory / md5)
        if (fileSystem.exists(filePath)) {
            return md5.toPath()
        }
        fileSystem.createDirectory(platformValues.iconsDirectory)
        logger.debug("icon file $filePath does not exists")
        fileSystem.write(filePath) {
            write(data)
        }
        return md5.toPath()
    }

    private suspend fun linkSongWithIcon(song: SongId, relativePath: Path) {
        db.executeOrDefault(MainDbSetup.updateSongIconPath(song, relativePath.toString()))
    }
}