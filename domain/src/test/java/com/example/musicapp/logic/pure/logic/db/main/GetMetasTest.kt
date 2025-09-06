package com.example.musicapp.logic.pure.logic.db.main

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.pure.query.Arg
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import com.example.musicapp.domain.logic.pure.sql.mainDb.Meta
import junit.framework.TestCase.assertEquals
import org.junit.Test

class GetMetasTest {
    @Test
    fun getAllMetas_empty_sql() {
        assertEquals(
            "",
            Meta.getAllMetas(emptyList()).sql,
        )
    }

    @Test
    fun getAllMetas_empty_args() {
        assertEquals(
            emptyList<Arg>(),
            Meta.getAllMetas(emptyList()).bindArgs,
        )
    }

    @Test
    fun getAllMetas_1Song_sql() {
        assertEquals(
            "SELECT song_id,key,value FROM meta WHERE song_id IN (1)",
            Meta.getAllMetas(listOf(SongId(1))).sql,
        )
    }

    @Test
    fun getAllMetas_1Song_args() {
        assertEquals(
            emptyList<Arg>(),
            Meta.getAllMetas(listOf(SongId(1))).bindArgs,
        )
    }

    @Test
    fun getAllMetas_3Songs_sql() {
        assertEquals(
            "SELECT song_id,key,value FROM meta WHERE song_id IN (1,2,3)",
            Meta.getAllMetas(listOf<Long>(1,2,3).map(::SongId)).sql,
        )
    }

    @Test
    fun getMetas_3Songs_2Tags_sql() {
        assertEquals(
            "SELECT song_id,key,value FROM meta WHERE key IN ('key1','key2') AND song_id IN (1,2,3)",
            Meta.getMetas(
                listOf<Long>(1,2,3).map(::SongId),
                listOf("key1", "key2").map(::MetaKey)
            ).sql,
        )
    }
}