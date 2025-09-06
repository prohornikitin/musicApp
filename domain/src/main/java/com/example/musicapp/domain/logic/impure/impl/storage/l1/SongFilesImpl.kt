package com.example.musicapp.domain.logic.impure.impl.storage.l1

import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.SongFiles
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import java.io.File
import javax.inject.Inject

class SongFilesImpl @Inject constructor(
    private val db: DbQueryInterpreter
) : SongFiles {
    override fun getById(id: SongId) = db.execute(MainDbSetup.getFileBySongId(id))?.let(::File)

    override fun getAllByIds(ids: List<SongId>) =
        db.execute(MainDbSetup.getFilesPathsBySongId(ids)).associate {
            Pair(it.first, File(it.second))
        }
}