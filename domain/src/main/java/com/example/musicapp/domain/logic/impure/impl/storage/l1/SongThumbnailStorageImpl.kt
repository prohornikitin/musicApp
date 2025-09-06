package com.example.musicapp.domain.logic.impure.impl.storage.l1

import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import javax.inject.Inject

class SongThumbnailStorageImpl @Inject constructor(
    private val db: DbQueryInterpreter
) : SongThumbnailStorage {
    override fun get(id: SongId) = db.execute(MainDbSetup.getIconPath(id))
}