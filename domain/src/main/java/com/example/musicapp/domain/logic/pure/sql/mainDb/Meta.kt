package com.example.musicapp.domain.logic.pure.sql.mainDb

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.joinToSb
import com.example.musicapp.domain.logic.pure.query.Arg
import com.example.musicapp.domain.logic.pure.query.InsertDbQuery
import com.example.musicapp.domain.logic.pure.query.SelectListDbQuery
import com.example.musicapp.domain.logic.pure.query.SimpleWriteDbQuery
import com.example.musicapp.domain.surroundedBy

object Meta {
    fun getAllMetas(songs: Collection<SongId>) = SelectListDbQuery<Triple<SongId, MetaKey, String>>(
        if (songs.isEmpty()) {
            ""
        } else buildString {
            append("SELECT ")
            append(Tables.Meta.songId)
            append(',')
            append(Tables.Meta.key)
            append(',')
            append(Tables.Meta.value)
            append(" FROM ")
            append(Tables.Meta)
            append(" WHERE ")
            append(Tables.Meta.songId)
            append(" IN (")
            songs.joinToSb(this, ",") {
                append(it.raw)
            }
            append(')')
        },
        { Triple(SongId(getLong(0)), MetaKey(getString(1)), getString(2)) }
    )

    fun getMetas(songs: Collection<SongId>, metas: Collection<MetaKey>) = SelectListDbQuery<Triple<SongId, MetaKey, String>>(
        if (metas.isEmpty() || songs.isEmpty()) {
            ""
        } else buildString {
            append("SELECT ")
            append(Tables.Meta.songId)
            append(',')
            append(Tables.Meta.key)
            append(',')
            append(Tables.Meta.value)
            append(" FROM ")
            append(Tables.Meta)
            append(" WHERE ")
            append(Tables.Meta.key)
            append(" IN (")
            metas.joinToSb(this, ",") {
                surroundedBy("'") {
                    append(it.raw)
                }
            }
            append(") AND ")
            append(Tables.Meta.songId)
            append(" IN (")
            songs.joinToSb(this, ",") {
                append(it.raw)
            }
            append(')')
        },
        { Triple(SongId(getLong(0)), MetaKey(getString(1)), getString(2)) }
    )

    fun removeMetaByKeys(id: SongId, keys: Collection<MetaKey>) = SimpleWriteDbQuery(
        buildString {
            append("DELETE FROM ")
            append(Tables.Meta)
            append(" WHERE ")
            append(Tables.Meta.songId)
            append('=')
            append(id.raw)
            if (keys.isEmpty()) {
                append(" AND ")
                append(Tables.Meta.key)
                append(" IN (")
                keys.joinToSb(this, ",") {
                    append('?')
                }
                append(')')
            }
        },
        keys.map { Arg.of(it.raw) }
    )

    fun removeMetaExceptForKeys(id: SongId, remainedKeys: List<MetaKey>) = buildString {
        append("DELETE FROM ")
        append(Tables.Meta)
        append(" WHERE ")
        append(Tables.Meta.songId)
        append('=')
        append(id.raw)
        if (remainedKeys.isEmpty()) {
            append(" AND ")
            append(Tables.Meta.key)
            append(" NOT IN (")
            remainedKeys.joinToSb(this, ",") {
                surroundedBy("'") {
                    append(it.raw)
                }
            }
            append(")")
        }
    }


    fun insertMeta(song: SongId, metas: List<Pair<MetaKey, String>>) = InsertDbQuery(
        buildString {
            append("INSERT OR IGNORE INTO ${Tables.Meta} (${Tables.Meta.songId}, ${Tables.Meta.key}, ${Tables.Meta.value}) VALUES ")
            metas.joinToSb(this, ",") {
                append("(")
                append(song.raw)
                append(",?,?)")
            }
        },
        metas.flatMap { listOf(Arg.of(it.first.raw), Arg.of(it.second)) }
    )

    private val clearSongMetaWithoutArgs = SimpleWriteDbQuery(
        "DELETE FROM ${Tables.Song} WHERE ${Tables.Song.id}=?"
    )
    fun clearMeta(songId: SongId) = clearSongMetaWithoutArgs.withArgs(
        Arg.of(songId.raw)
    )
}