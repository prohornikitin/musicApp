package afc.musicapp.data.pure.sql.mainDb

import afc.musicapp.domain.entities.SongCardText
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.escapeSqlString
import afc.musicapp.data.impure.iface.db.query.Arg
import afc.musicapp.data.impure.iface.db.query.SelectListDbQuery
import afc.musicapp.data.impure.iface.db.query.SimpleWriteDbQuery
import afc.musicapp.data.pure.sql.Basic.inClause
import afc.musicapp.domain.surroundedBy

object Templates {
    fun updateGeneratedTemplates(map: Map<SongId, SongCardText>) = SimpleWriteDbQuery(
        if (map.isEmpty()) {
            ""
        } else buildString {
            append("REPLACE INTO ")
            append(Tables.GenTemplate)
            append(" (")
            append(Tables.GenTemplate.songId)
            append(',')
            append(Tables.GenTemplate.main)
            append(',')
            append(Tables.GenTemplate.sub)
            append(',')
            append(Tables.GenTemplate.mainLowercase)
            append(',')
            append(Tables.GenTemplate.subLowercase)
            append(") VALUES (?,?,?,?,?)")
            (2..map.entries.size).forEach {
                append(",(?,?,?,?,?)")
            }
        },
        map.flatMap {
            listOf(
                Arg.Companion.of(it.key.raw),
                Arg.Companion.of(it.value.main),
                Arg.Companion.of(it.value.sub),
                Arg.Companion.of(it.value.main.lowercase()),
                Arg.Companion.of(it.value.sub.lowercase()),
            )
        }
    )

    fun insertNewTemplate(id: SongId, main: String, sub: String) = buildString {
        append("INSERT INTO ")
        append(Tables.GenTemplate)
        append(" (")
        append(Tables.GenTemplate.songId)
        append(',')
        append(Tables.GenTemplate.main)
        append(',')
        append(Tables.GenTemplate.sub)
        append(") VALUES (")
        append(id.raw)
        append(',')
        surroundedBy("'") {
            append(main.escapeSqlString())
        }
        append(',')
        surroundedBy("'") {
            append(sub.escapeSqlString())
        }
        append(')')
    }

    private val removeTemplateWithoutArgs = SimpleWriteDbQuery(
        buildString {
            append("DELETE FROM ")
            append(Tables.GenTemplate)
            append("WHERE")
            append(Tables.GenTemplate.songId)
            append("=?")
        },
    )

    fun removeTemplate(song: SongId) = removeTemplateWithoutArgs.withArgs(
        Arg.of(song.raw)
    )

    fun getSongCardTextFor(ids: List<SongId>) = SelectListDbQuery<Pair<SongId, SongCardText>>(
        if (ids.isEmpty()) {
            ""
        } else buildString {
            append(
                "SELECT ${Tables.GenTemplate.songId}, ${Tables.GenTemplate.main}, ${Tables.GenTemplate.sub} " +
                        "FROM ${Tables.GenTemplate} " +
                        "WHERE ${Tables.GenTemplate.songId} "
            )
            inClause(ids) {
                append(it.raw)
            }
        },
        {
            Pair(
                SongId(getLong(0)),
                SongCardText(
                    main = getString(1),
                    sub = getString(2),
                )
            )
        }
    )
}