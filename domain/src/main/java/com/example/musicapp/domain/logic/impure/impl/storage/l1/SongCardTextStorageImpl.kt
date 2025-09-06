package com.example.musicapp.domain.logic.impure.impl.storage.l1

import com.example.musicapp.domain.data.SongCardText
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.SongCardTextStorage
import com.example.musicapp.domain.logic.impure.iface.storage.l1.write.SongCardTextUpdate
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import javax.inject.Inject

class SongCardTextStorageImpl @Inject constructor(
    private val db: DbQueryInterpreter,
) : SongCardTextStorage, SongCardTextUpdate {
    override fun get(ids: List<SongId>): Map<SongId, SongCardText> =
        db.execute(MainDbSetup.getSongCardTextFor(ids)).associate {
            Pair(
                it.first,
                SongCardText(
                    main = it.second.main,
                    sub = it.second.sub,
                ),
            )
        }

    override fun update(id: SongId, text: SongCardText) = db.execute(
        MainDbSetup.updateGeneratedTemplates(mapOf(
            id to text
        ))
    )
}