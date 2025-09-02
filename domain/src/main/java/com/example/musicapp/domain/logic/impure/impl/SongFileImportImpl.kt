package com.example.musicapp.domain.logic.impure.impl

import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.MetaParser
import com.example.musicapp.domain.logic.impure.iface.SongFileImport
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSql
import java.io.File

class SongFileImportImpl(
    private val db: DbQueryInterpreter,
    private val metaParser: MetaParser,
) : SongFileImport {
    override fun importIfFileNotExists(file: File)  {
        val newSongId = db.execute(MainDbSql.insertSongFile(file.path))?.let(::SongId)
        if (newSongId == null) {
            return
        }
        val meta = metaParser.getFullMetaFromFile(file)
        val iconId = if(meta.icon != null) {
            db.execute(MainDbSql.insertIcon(meta.icon)) ?: db.execute(MainDbSql.findIconIdByContent(meta.icon))
        } else null
        if(iconId != null) {
            db.execute(MainDbSql.updateSongIcon(newSongId, iconId))
        }
    }

    override fun reloadDataFromFile(file: File) {
        TODO("Not yet implemented")
    }
}