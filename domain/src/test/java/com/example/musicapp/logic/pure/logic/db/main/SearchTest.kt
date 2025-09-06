package com.example.musicapp.logic.pure.logic.db.main

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SearchQuery
import com.example.musicapp.domain.logic.pure.query.Arg
import com.example.musicapp.domain.logic.pure.sql.mainDb.SongsQuery
import junit.framework.TestCase.assertEquals
import org.junit.Test

class SearchTest {
    @Test
    fun empty_sql() {
        assertEquals("SELECT song_id FROM gen_template", SongsQuery.search(SearchQuery(emptyMap(), "")).sql)
    }

    @Test
    fun empty_args() {
        assertEquals(emptyList<Arg>(), SongsQuery.search(SearchQuery(emptyMap(), "")).bindArgs)
    }

    @Test
    fun byText_sql() {
        assertEquals(
            "SELECT song_id FROM gen_template WHERE main LIKE 'query%' OR sub LIKE 'query%'",
            SongsQuery.search(SearchQuery(emptyMap(), "query")).sql,
        )
    }

    @Test
    fun byText_args() {
        assertEquals(emptyList<Arg>(), SongsQuery.search(SearchQuery(emptyMap(), "query")).bindArgs)
    }

    @Test
    fun byMetaPrefixes_sql() {
        assertEquals(
            "SELECT song_id FROM gen_template WHERE song_id IN (" +
                "SELECT song_id FROM meta WHERE " +
                "key='TITLE' AND value LIKE 'title%'" +
                " GROUP BY song_id HAVING COUNT(song_id)>=1" +
            ")",
            SongsQuery.search(
                SearchQuery(mapOf(MetaKey("TITLE") to "title"), "")
            ).sql,
        )
    }

    @Test
    fun byMetaPrefixes_args() {
        assertEquals(
            emptyList<Arg>(),
            SongsQuery.search(
                SearchQuery(mapOf(MetaKey("TITLE") to "title"), "")
            ).bindArgs,
        )
    }
}