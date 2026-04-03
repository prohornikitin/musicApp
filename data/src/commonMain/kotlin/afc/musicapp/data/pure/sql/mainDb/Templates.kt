package afc.musicapp.data.pure.sql.mainDb

import afc.musicapp.domain.entities.SongCardText
import afc.musicapp.domain.entities.SongId
import afc.musicapp.data.impure.iface.db.query.Arg
import afc.musicapp.data.impure.iface.db.query.SelectListDbQuery
import afc.musicapp.data.impure.iface.db.query.SimpleWriteDbQuery
import afc.musicapp.data.pure.sql.Basic.inClause

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
            repeat(map.entries.size - 1) {
                append(",(?,?,?,?,?)")
            }
        },
        map.flatMap {
            listOf(
                Arg.of(it.key.raw),
                Arg.of(it.value.main),
                Arg.of(it.value.sub),
                Arg.of(it.value.main.lowercase()),
                Arg.of(it.value.sub.lowercase()),
            )
        }
    )

    private val removeTemplateWithoutArgs = SimpleWriteDbQuery(
        "DELETE FROM ${Tables.GenTemplate} WHERE ${Tables.GenTemplate.songId}=?"
    )

    fun removeTemplate(song: SongId) = removeTemplateWithoutArgs.withArgs(
        Arg.of(song.raw)
    )

    fun getSongCardTextFor(ids: List<SongId>) = SelectListDbQuery(
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