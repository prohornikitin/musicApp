package afc.musicapp.data

import afc.musicapp.domain.entities.SongId
import afc.musicapp.data.impure.iface.db.DbQueryInterpreter
import afc.musicapp.domain.logic.impure.iface.storage.read.SongFiles
import afc.musicapp.data.pure.sql.mainDb.Songs
import okio.Path.Companion.toPath

class SongFilesImpl(
    private val db: DbQueryInterpreter
) : SongFiles {
    override suspend fun getById(id: SongId) = db.executeOrDefault(Songs.getFileBySongId(id))?.toPath()

    override suspend fun getAllByIds(ids: List<SongId>) =
        db.executeOrDefault(Songs.getFilesPathsBySongId(ids)).associate {
            Pair(it.first, it.second.toPath())
        }
}