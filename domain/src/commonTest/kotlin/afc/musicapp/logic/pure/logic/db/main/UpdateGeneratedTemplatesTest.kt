package com.example.musicapp.logic.pure.logic.db.main

import afc.musicapp.domain.entities.SongCardText
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.logic.pure.sql.query.Arg
import afc.musicapp.domain.logic.pure.sql.query.SimpleWriteDbQuery
import afc.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import kotlin.test.Test
import kotlin.test.assertEquals

class UpdateGeneratedTemplatesTest {
    @Test
    fun updateGeneratedTemplates_empty() {
        assertEquals(
            SimpleWriteDbQuery(""),
            MainDbSetup.updateGeneratedTemplates(emptyMap()),
        )
    }

    @Test
    fun updateGeneratedTemplates_1song() {
        assertEquals(
            SimpleWriteDbQuery(
                "REPLACE INTO gen_template (song_id,main,sub) VALUES (?,?,?)",
                listOf(Arg.of(15), Arg.of("main"), Arg.of("sub")),
            ),
            MainDbSetup.updateGeneratedTemplates(mapOf(
                SongId(15) to SongCardText("main", "sub")
            )),
        )
    }

    @Test
    fun updateGeneratedTemplates_2songs() {
        assertEquals(
            SimpleWriteDbQuery(
                "REPLACE INTO gen_template (song_id,main,sub) VALUES (?,?,?),(?,?,?)",
                listOf(Arg.of(1), Arg.of("main1"), Arg.of("sub1"), Arg.of(2), Arg.of("main2"), Arg.of("sub2")),
            ),
            MainDbSetup.updateGeneratedTemplates(mapOf(
                SongId(1) to SongCardText("main1", "sub1"),
                SongId(2) to SongCardText("main2", "sub2"),
            )),
        )
    }
}