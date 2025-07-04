package com.example.musicapp.logic.pure.logic.db.main

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SearchQuery
import com.example.musicapp.domain.logic.pure.query.Arg
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSql
import com.example.musicapp.domain.logic.pure.sql.mainDb.Tables
import com.example.musicapp.domain.logic.pure.sql.mainDb.Tables.GenTemplate
import junit.framework.TestCase.assertEquals
import org.junit.Test

class SearchTest {
    @Test
    fun empty_sql() {
        assertEquals("SELECT ${GenTemplate.songId} FROM $GenTemplate", MainDbSql.search(SearchQuery(emptyMap(), "")).sql)
    }

    @Test
    fun empty_args() {
        assertEquals(emptyList<Arg>(), MainDbSql.search(SearchQuery(emptyMap(), "")).bindArgs)
    }

    @Test
    fun byText_sql() {
        assertEquals(
            "SELECT ${GenTemplate.songId} FROM $GenTemplate WHERE ${GenTemplate.main} LIKE 'query%' OR ${GenTemplate.sub} LIKE 'query%'",
            MainDbSql.search(SearchQuery(emptyMap(), "query")).sql,
        )
    }

    @Test
    fun byText_args() {
        assertEquals(emptyList<Arg>(), MainDbSql.search(SearchQuery(emptyMap(), "query")).bindArgs)
    }

    @Test
    fun byMetaPrefixes_sql() {
        assertEquals(
            "SELECT ${GenTemplate.songId} FROM $GenTemplate WHERE ${GenTemplate.songId} IN (" +
                "SELECT ${Tables.Meta.songId} FROM ${Tables.Meta} WHERE " +
                "${Tables.Meta.key}='TITLE' AND ${Tables.Meta.value} LIKE 'title%'" +
                " GROUP BY ${Tables.Meta.songId} HAVING COUNT(${Tables.Meta.songId})>=1" +
            ")",
            MainDbSql.search(
                SearchQuery(mapOf(MetaKey("TITLE") to "title"), "")
            ).sql,
        )
    }

    @Test
    fun byMetaPrefixes_args() {
        assertEquals(
            emptyList<Arg>(),
            MainDbSql.search(
                SearchQuery(mapOf(MetaKey("TITLE") to "title"), "")
            ).bindArgs,
        )
    }
}