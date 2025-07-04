package com.example.musicapp.domain.logic.impure.impl

import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.impl.logger.Logger
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.impure.iface.storage.v2.read.SongFiles
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSql
import java.io.File

class SongFilesImpl(
    val dbQueryInterpreter: DbQueryInterpreter,
    val logger: Logger,
) : SongFiles {
    override fun getById(id: SongId): File? {
        return dbQueryInterpreter.execute(MainDbSql.getFileBySongId(id))?.let(::File)
    }

    override fun getAllByIds(ids: List<SongId>): Map<SongId, File> {
        return dbQueryInterpreter.execute(MainDbSql.getFilesPathsBySongId(ids))
            .toMap()
            .mapValues { File(it.value) }
    }
}