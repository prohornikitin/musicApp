package com.example.musicapp.logic.pure.logic.db.main

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.pure.query.Arg
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSql
import com.example.musicapp.domain.logic.pure.sql.mainDb.Tables
import com.example.musicapp.logic.pure.logic.assertCharSeqEquals
import junit.framework.TestCase.assertEquals
import org.junit.Test

class OtherTest {
    @Test
    fun getFilesPathsBySongId_empty_sql() {
        assertCharSeqEquals(
            "",
            MainDbSql.getFilesPathsBySongId(emptyList()).sql,
        )
    }

    @Test
    fun getFilesPathsBySongId_empty_args() {
        assertEquals(
            emptyList<Arg>(),
            MainDbSql.getFilesPathsBySongId(emptyList()).bindArgs,
        )
    }


    @Test
    fun getFilesPathsBySongId_2Ids_sql() {
        assertCharSeqEquals(
            "SELECT ${Tables.Song.id},${Tables.Song.musicFilePath} FROM ${Tables.Song} WHERE ${Tables.Song.id} IN (-2,10)",
            MainDbSql.getFilesPathsBySongId(listOf(-2L, 10L).map(::SongId)).sql,
        )
    }

    @Test
    fun getFilesPathsBySongId_2Ids_args() {
        assertEquals(
            emptyList<Arg>(),
            MainDbSql.getFilesPathsBySongId(listOf(-2L, 10L).map(::SongId)).bindArgs,
        )
    }

    @Test
    fun getMetasForAll_empty_sql() {
        assertCharSeqEquals(
            "",
            MainDbSql.getMetasForAll(emptySet()).sql,
        )
    }

    @Test
    fun getMetasForAll_empty_args() {
        assertEquals(
            emptyList<Arg>(),
            MainDbSql.getMetasForAll(emptySet()).bindArgs,
        )
    }

    @Test
    fun getMetasForAll_norm_sql() {
        assertCharSeqEquals(
            "SELECT ${Tables.Meta.songId},${Tables.Meta.key},${Tables.Meta.value} FROM ${Tables.Meta} WHERE ${Tables.Meta.key} IN ('key')",
            MainDbSql.getMetasForAll(setOf(MetaKey("key"))).sql,
        )
    }

    @Test
    fun getMetasForAll_norm_args() {
        assertEquals(
            emptyList<Arg>(),
            MainDbSql.getMetasForAll(setOf(MetaKey("key"))).bindArgs,
        )
    }


}