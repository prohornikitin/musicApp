package afc.musicapp.data.pure.sql.mainDb

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SearchQuery
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.escapeSqlString
import afc.musicapp.data.pure.sql.Basic
import afc.musicapp.data.impure.iface.db.query.SelectListDbQuery
import afc.musicapp.data.pure.sql.Basic.joinToSb
import afc.musicapp.domain.surroundedBy

object SongsQuery {
    val queryAll = SelectListDbQuery(
        "SELECT ${Tables.Song.id} FROM ${Tables.Song}",
        { SongId(getLong(0)) }
    )

    private fun searchByTextCondition(
        text: String,
    ) : CharSequence {
        if (text.isEmpty()) {
            return ""
        }
        val preparedText = text.lowercase().escapeSqlString()
        return buildString {
            append(Tables.GenTemplate.mainLowercase)
            append(" LIKE ")
            surroundedBy("'") {
                append(preparedText)
                append('%')
            }
            append(" OR ")
            append(Tables.GenTemplate.subLowercase)
            append(" LIKE ")
            surroundedBy("'") {
                append(preparedText)
                append('%')
            }
        }
    }

    private fun searchByMetaPrefixesCondition(prefixes: Map<MetaKey, String>) : CharSequence {
        if(prefixes.isEmpty()) {
            return ""
        }
        return buildString {
            append(Tables.GenTemplate.songId)
            append(" IN (")
            append("SELECT ")
            append(Tables.Meta.songId)
            append(" FROM ")
            append(Tables.Meta)

            append(" WHERE ")
            prefixes.entries.joinToSb(this, ") OR (") {
                append(Tables.Meta.key)
                append('=')
                surroundedBy("'") {
                    append(it.key.raw.escapeSqlString())
                }
                append(" AND ")
                append(Tables.Meta.value)
                append(" LIKE ")
                surroundedBy("'") {
                    append(it.value.escapeSqlString())
                    append('%')
                }
            }
            append(" GROUP BY ")
            append(Tables.Meta.songId)
            append(" HAVING ")
            append("COUNT(")
            append(Tables.Meta.songId)
            append(")>=")
            append(prefixes.size)
            append(')')
        }
    }

    fun search(query: SearchQuery) = SelectListDbQuery(
        buildString {
            append("SELECT DISTINCT ")
            append(Tables.GenTemplate.songId)
            append(" FROM ")
            append(Tables.GenTemplate)
            if (!query.isEmpty()) {
                append(" WHERE ")
                append(
                    Basic.andMany(
                        listOf(
                            searchByTextCondition(query.plain),
                            searchByMetaPrefixesCondition(query.metaPrefixes),
                        )
                    )
                )
            }
        },
        { SongId(getLong(0)) }
    )
}