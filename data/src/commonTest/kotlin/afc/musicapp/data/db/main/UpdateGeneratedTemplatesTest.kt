package afc.musicapp.data.db.main

import afc.musicapp.domain.entities.SongCardText
import afc.musicapp.domain.entities.SongId
import afc.musicapp.data.pure.sql.mainDb.Templates
import afc.musicapp.data.impure.iface.db.query.Arg
import afc.musicapp.data.impure.iface.db.query.SimpleWriteDbQuery
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateGeneratedTemplatesTest {
    @Test
    fun updateGeneratedTemplates_empty() {
        assertEquals(
            SimpleWriteDbQuery(""),
            Templates.updateGeneratedTemplates(emptyMap()),
        )
    }

    @Test
    fun updateGeneratedTemplates_1song() {
        assertEquals(
            SimpleWriteDbQuery(
                "REPLACE INTO gen_template (song_id,main,sub,main_lower,sub_lower) VALUES (?,?,?,?,?)",
                listOf(Arg.of(15), Arg.of("Main"), Arg.of("Sub"), Arg.of("main"), Arg.of("sub")),
            ),
            Templates.updateGeneratedTemplates(mapOf(
                SongId(15) to SongCardText("Main", "Sub")
            )),
        )
    }

    @Test
    fun updateGeneratedTemplates_2songs() {
        assertEquals(
            SimpleWriteDbQuery(
                "REPLACE INTO gen_template (song_id,main,sub,main_lower,sub_lower) VALUES (?,?,?,?,?),(?,?,?,?,?)",
                listOf(
                    Arg.of(1), Arg.of("Main1"), Arg.of("Sub1"), Arg.of("main1"), Arg.of("sub1"),
                    Arg.of(2), Arg.of("Main2"), Arg.of("Sub2"), Arg.of("main2"), Arg.of("sub2"),
                ),
            ),
            Templates.updateGeneratedTemplates(mapOf(
                SongId(1) to SongCardText("Main1", "Sub1"),
                SongId(2) to SongCardText("Main2", "Sub2"),
            )),
        )
    }
}