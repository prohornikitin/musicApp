package com.example.musicapp.domain.logic.impure.impl.storage.l2

import com.example.musicapp.domain.data.ConfigKey
import com.example.musicapp.domain.data.SongCardText
import com.example.musicapp.domain.data.Template
import com.example.musicapp.domain.logic.impure.iface.FormattedMetaRead
import com.example.musicapp.domain.logic.impure.iface.SongSearch
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.impure.iface.storage.l1.write.KvConfigUpdate
import com.example.musicapp.domain.logic.impure.CardTemplatesEdit
import com.example.musicapp.domain.logic.pure.applyTemplate
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import javax.inject.Inject

class CardTemplatesEditImpl @Inject constructor(
    private val db: DbQueryInterpreter,
    val configUpdate: KvConfigUpdate,
    private val formattedMetaStorage: FormattedMetaRead,
    private val search: SongSearch,
) : CardTemplatesEdit {
    companion object {
        const val CHUNK_SIZE = 100
    }

    override fun update(main: Template, sub: Template) {
        configUpdate.update(ConfigKey.MAIN_TEMPLATE, main.toString())
        configUpdate.update(ConfigKey.SUB_TEMPLATE, sub.toString())
        val usedMetaKeys = main.getUsedKeys() + sub.getUsedKeys()
        search.allSongs().chunked(CHUNK_SIZE).forEach {
            val templates = formattedMetaStorage.getFields(it, usedMetaKeys).mapValues {
                SongCardText(
                    applyTemplate(main, it.value),
                    applyTemplate(sub, it.value),
                )
            }
            db.execute(MainDbSetup.updateGeneratedTemplates(templates))
        }
    }
}