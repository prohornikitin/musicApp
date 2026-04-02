package afc.musicapp.data.db.main

import afc.musicapp.domain.entities.SongId
import afc.musicapp.data.impure.iface.db.query.Arg
import afc.musicapp.data.pure.sql.mainDb.Songs
import afc.musicapp.data.db.assertCharSeqEquals
import kotlin.test.Test
import kotlin.test.assertEquals

class GetFilesPathsBySongIdTest {
    @Test
    fun getFilesPathsBySongId_empty_sql() {
        assertCharSeqEquals(
            "",
            Songs.getFilesPathsBySongId(emptyList()).sql,
        )
    }

    @Test
    fun getFilesPathsBySongId_empty_args() {
        assertEquals(
            emptyList<Arg>(),
            Songs.getFilesPathsBySongId(emptyList()).bindArgs,
        )
    }

    @Test
    fun getFilesPathsBySongId_2Ids_sql() {
        assertCharSeqEquals(
            "SELECT song_id,file_path FROM song WHERE song_id IN (-2,10)",
            Songs.getFilesPathsBySongId(listOf(-2L, 10L).map(::SongId)).sql,
        )
    }

    @Test
    fun getFilesPathsBySongId_2Ids_args() {
        assertEquals(
            emptyList<Arg>(),
            Songs.getFilesPathsBySongId(listOf(-2L, 10L).map(::SongId)).bindArgs,
        )
    }
}