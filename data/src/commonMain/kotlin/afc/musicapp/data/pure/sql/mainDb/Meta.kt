package afc.musicapp.data.pure.sql.mainDb

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SongId
import afc.musicapp.data.impure.iface.db.query.Arg
import afc.musicapp.data.impure.iface.db.query.InsertDbQuery
import afc.musicapp.data.impure.iface.db.query.SelectListDbQuery
import afc.musicapp.data.impure.iface.db.query.SimpleWriteDbQuery
import afc.musicapp.data.pure.sql.Basic.inClause
import afc.musicapp.data.pure.sql.Basic.joinToSb
import afc.musicapp.domain.surroundedBy

object Meta {
    fun getAllMetas(songs: Collection<SongId>) = SelectListDbQuery(
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
            inClause(songs) { append(it.raw) }
        },
        { Triple(SongId(getLong(0)), MetaKey(getString(1)), getString(2)) }
    )

    fun getMetasForAllSongsPageableBySong(keys: Collection<MetaKey>, page: Long, pageSize: Long = 100): SelectListDbQuery<Triple<SongId, MetaKey, String>> {
//        assert(pageSize <= 0)
        return SelectListDbQuery(
            if (keys.isEmpty()) {
                ""
            } else buildString {
                append("SELECT ")
                append("id,")
                append(Tables.Meta.key)
                append(',')
                append(Tables.Meta.value)
                append(" FROM (")
                    append("SELECT ")
                    append(Tables.Song.id)
                    append(" as id FROM ")
                    append(Tables.Song)
                    append(" ORDER BY id")
                    append(" LIMIT ")
                    append(pageSize)
                    append(" OFFSET ")
                    append(pageSize * page)
                    append(")")
                append(" JOIN ")
                append(Tables.Meta)
                append(" ON id=")
                append(Tables.Meta.songId.fullName)
                append(" WHERE ")
                append(Tables.Meta.key)
                inClause(keys) {
                    surroundedBy("'") {
                        it.raw
                    }
                }
            },
            { Triple(SongId(getLong(0)), MetaKey(getString(1)), getString(2)) }
        )
    }

    fun getMetas(songs: Collection<SongId>, metas: Collection<MetaKey>) =
        SelectListDbQuery(
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
                inClause(metas) {
                    surroundedBy("'") {
                        append(it.raw)
                    }
                }
                append(" AND ")
                append(Tables.Meta.songId)
                append(" IN (")
                songs.joinToSb(this, ",") {
                    append(it.raw)
                }
                append(')')
            },
            { Triple(SongId(getLong(0)), MetaKey(getString(1)), getString(2)) }
        )


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
        metas.flatMap { listOf(Arg.of(it.first.raw), Arg.of(it.second)) }
    )

    private val clearSongMetaWithoutArgs = SimpleWriteDbQuery(
        "DELETE FROM ${Tables.Meta} WHERE ${Tables.Song.id}=?"
    )
    fun clearMeta(songId: SongId) = clearSongMetaWithoutArgs.withArgs(
        Arg.of(songId.raw)
    )
}