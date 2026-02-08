package com.example.musicapp.logic.pure.logic.db.main

import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.logic.pure.sql.query.Arg
import afc.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import com.example.musicapp.logic.pure.logic.assertCharSeqEquals
import kotlin.test.Test
import kotlin.test.assertEquals

class GetFilesPathsBySongIdTest {
    @Test
    fun getFilesPathsBySongId_empty_sql() {
        assertCharSeqEquals(
            "",
            MainDbSetup.getFilesPathsBySongId(emptyList()).sql,
        )
    }

    @Test
    fun getFilesPathsBySongId_empty_args() {
        assertEquals(
            emptyList<Arg>(),
            MainDbSetup.getFilesPathsBySongId(emptyList()).bindArgs,
        )
    }

    @Test
    fun getFilesPathsBySongId_2Ids_sql() {
        assertCharSeqEquals(
            "SELECT song_id,file_path FROM song WHERE song_id IN (-2,10)",
            MainDbSetup.getFilesPathsBySongId(listOf(-2L, 10L).map(::SongId)).sql,
        )
    }

    @Test
    fun getFilesPathsBySongId_2Ids_args() {
        assertEquals(
            emptyList<Arg>(),
            MainDbSetup.getFilesPathsBySongId(listOf(-2L, 10L).map(::SongId)).bindArgs,
        )
    }
}