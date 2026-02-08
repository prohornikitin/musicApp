package afc.musicapp.domain.logic.impure.impl.storage

import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import afc.musicapp.domain.logic.impure.iface.storage.read.SongFiles
import afc.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import okio.Path.Companion.toPath

class SongFilesImpl(
    private val db: DbQueryInterpreter
) : SongFiles {
    override suspend fun getById(id: SongId) = db.executeOrDefault(MainDbSetup.getFileBySongId(id))?.toPath()

    override suspend fun getAllByIds(ids: List<SongId>) =
        db.executeOrDefault(MainDbSetup.getFilesPathsBySongId(ids)).associate {
            Pair(it.first, it.second.toPath())
        }
}