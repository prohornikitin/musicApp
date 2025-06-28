package com.example.musicapp.logic.pure.logic.sql

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SearchQuery
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.GEN_TEMPLATE_MAIN
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.GEN_TEMPLATE_SONG
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.GEN_TEMPLATE_SUB
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.SONG_FILE_FILE
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.SONG_FILE_SONG
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.SONG_META_KEY
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.SONG_META_SONG
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.SONG_META_VALUE
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.TABLE_GEN_TEMPLATE
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.TABLE_SONG_FILE
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.TABLE_SONG_META
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.getFileBySongId
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.getFilesBySongId
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.getMetasForAll
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.replaceMetas
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.search
import org.junit.Test

class SongsDbSqlTest {
    @Test
    fun search_empty() {
        assertCharSeqEquals("", search(emptyList(), SearchQuery(emptyMap(), "")))
    }

    @Test
    fun search_fromList() {
        assertCharSeqEquals(
            "SELECT $GEN_TEMPLATE_SONG FROM $TABLE_GEN_TEMPLATE WHERE $GEN_TEMPLATE_SONG IN (1)",
            search(listOf(SongId(1)), SearchQuery(emptyMap(), "")),
        )
    }

    @Test
    fun search_byText() {
        assertCharSeqEquals(
            "SELECT $GEN_TEMPLATE_SONG FROM $TABLE_GEN_TEMPLATE WHERE (" +
                "$GEN_TEMPLATE_SONG IN (1)" +
                " AND ($GEN_TEMPLATE_MAIN LIKE 'query%' OR $GEN_TEMPLATE_SUB LIKE 'query%')" +
            ")",
            search(listOf(SongId(1)), SearchQuery(emptyMap(), "query")),
        )
    }

    @Test
    fun search_byMetaPrefixes() {
        assertCharSeqEquals(
            "SELECT $GEN_TEMPLATE_SONG FROM $TABLE_GEN_TEMPLATE WHERE (" +
                "$GEN_TEMPLATE_SONG IN (1) AND $GEN_TEMPLATE_SONG IN (" +
                    "SELECT $SONG_META_SONG FROM $TABLE_SONG_META WHERE " +
                        "$SONG_META_KEY='TITLE' AND $SONG_META_VALUE LIKE 'title%'" +
                    " GROUP BY $SONG_META_SONG HAVING COUNT($SONG_META_SONG)=1" +
                ")" +
            ")",
            search(listOf(SongId(1)), SearchQuery(mapOf(MetaKey("TITLE") to "title"), "")),
        )
    }

    @Test
    fun replaceMetas_empty() {
        assert(replaceMetas(SongId(1), emptyMap()).isEmpty())
    }

    @Test
    fun replaceMetas_tags() {
        assertCharSeqEquals(
            "REPLACE INTO $TABLE_SONG_META ($SONG_META_SONG,$SONG_META_KEY,$SONG_META_VALUE) VALUES (1,'key','value')",
            replaceMetas(
                SongId(1),
                mapOf(
                    MetaKey("key") to "value"
                )
            ),
        )
    }

    @Test
    fun getFileBySongId_test() {
        assertCharSeqEquals(
            "SELECT $SONG_FILE_FILE FROM $TABLE_SONG_FILE WHERE $SONG_FILE_SONG=1",
            getFileBySongId(SongId(1)),
        )
    }

    @Test
    fun getFilesBySongId_empty() {
        assertCharSeqEquals(
            "",
            getFilesBySongId(emptyList()),
        )
    }


    @Test
    fun getFilesBySongId_2ids() {
        assertCharSeqEquals(
            "SELECT $SONG_FILE_SONG,$SONG_FILE_FILE FROM $TABLE_SONG_FILE WHERE $SONG_FILE_SONG IN (-2,10)",
            getFilesBySongId(listOf(-2L, 10L).map(::SongId)),
        )
    }

    @Test
    fun getMetasForAll_empty() {
        assertCharSeqEquals(
            "",
            getMetasForAll(emptySet()),
        )
    }

    @Test
    fun getMetasForAll_norm() {
        assertCharSeqEquals(
            "SELECT song_id,key,value FROM song_meta WHERE key IN ('key')",
            getMetasForAll(setOf(MetaKey("key"))),
        )
    }

    @Test
    fun updateGeneratedTemplates() {
        assertCharSeqEquals(
            "REPLACE INTO gen_template (song_id,main,sub) VALUES (15,'main','sub')",
            SongsDbSql.updateGeneratedTemplates(mapOf(
                SongId(15) to Pair("main", "sub")
            )),
        )
    }
}