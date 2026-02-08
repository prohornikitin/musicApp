package afc.musicapp.domain.logic.pure.sql.mainDb

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.joinToSb
import afc.musicapp.domain.logic.pure.sql.query.Arg
import afc.musicapp.domain.logic.pure.sql.query.InsertDbQuery
import afc.musicapp.domain.logic.pure.sql.query.SelectListDbQuery
import afc.musicapp.domain.logic.pure.sql.query.SimpleWriteDbQuery
import afc.musicapp.domain.surroundedBy

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

    fun getMetas(songs: Collection<SongId>, metas: Collection<MetaKey>) =
        SelectListDbQuery<Triple<SongId, MetaKey, String>>(
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
        keys.map { Arg.Companion.of(it.raw) }
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
        if (metas.isEmpty()) {
            ""
        } else buildString {
            append("INSERT OR IGNORE INTO ${Tables.Meta} (${Tables.Meta.songId}, ${Tables.Meta.key}, ${Tables.Meta.value}) VALUES ")
            metas.joinToSb(this, ",") {
                append("(")
                append(song.raw)
                append(",?,?)")
            }
        },
        metas.flatMap { listOf(Arg.Companion.of(it.first.raw), Arg.Companion.of(it.second)) }
    )

    private val clearSongMetaWithoutArgs = SimpleWriteDbQuery(
        "DELETE FROM ${Tables.Song} WHERE ${Tables.Song.id}=?"
    )
    fun clearMeta(songId: SongId) = clearSongMetaWithoutArgs.withArgs(
        Arg.Companion.of(songId.raw)
    )
}