package afc.musicapp.data.db.main

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SearchQuery
import afc.musicapp.data.impure.iface.db.query.Arg
import afc.musicapp.data.pure.sql.mainDb.SongsQuery
import kotlin.test.Test
import kotlin.test.assertEquals

class SearchTest {
    @Test
    fun empty_sql() {
        assertEquals("SELECT DISTINCT song_id FROM gen_template", SongsQuery.search(SearchQuery(emptyMap(), "")).sql)
    }

    @Test
    fun empty_args() {
        assertEquals(emptyList<Arg>(), SongsQuery.search(SearchQuery(emptyMap(), "")).bindArgs)
    }

    @Test
    fun byText_sql() {
        assertEquals(
            "SELECT DISTINCT song_id FROM gen_template WHERE main_lower LIKE 'query%' OR sub_lower LIKE 'query%'",
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
            "SELECT DISTINCT song_id FROM gen_template WHERE song_id IN (" +
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