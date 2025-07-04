package com.example.musicapp.logic.pure.logic.db.main

import com.example.config.SongCardText
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.pure.query.Arg
import com.example.musicapp.domain.logic.pure.query.SimpleWriteDbQuery
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSql
import com.example.musicapp.domain.logic.pure.sql.mainDb.Tables.GenTemplate
import junit.framework.TestCase.assertEquals
import org.junit.Test

class UpdateGeneratedTemplatesTest {
    @Test
    fun updateGeneratedTemplates_empty() {
        assertEquals(
            SimpleWriteDbQuery(
                "",
            ),
            MainDbSql.updateGeneratedTemplates(emptyMap()),
        )
    }

    @Test
    fun updateGeneratedTemplates_1song() {
        assertEquals(
            SimpleWriteDbQuery(
                "REPLACE INTO gen_template (${GenTemplate.songId},${GenTemplate.main},${GenTemplate.sub}) VALUES (?,?,?)",
                listOf(Arg.of(15), Arg.of("main"), Arg.of("sub")),
            ),
            MainDbSql.updateGeneratedTemplates(mapOf(
                SongId(15) to SongCardText("main", "sub")
            )),
        )
    }

    @Test
    fun updateGeneratedTemplates_2songs() {
        assertEquals(
            SimpleWriteDbQuery(
                "REPLACE INTO gen_template (${GenTemplate.songId},${GenTemplate.main},${GenTemplate.sub}) VALUES (?,?,?),(?,?,?)",
                listOf(Arg.of(1), Arg.of("main1"), Arg.of("sub1"), Arg.of(2), Arg.of("main2"), Arg.of("sub2")),
            ),
            MainDbSql.updateGeneratedTemplates(mapOf(
                SongId(1) to SongCardText("main1", "sub1"),
                SongId(2) to SongCardText("main2", "sub2"),
            )),
        )
    }
}