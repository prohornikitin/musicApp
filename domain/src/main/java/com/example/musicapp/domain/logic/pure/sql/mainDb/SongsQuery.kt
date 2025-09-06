package com.example.musicapp.domain.logic.pure.sql.mainDb

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SearchQuery
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.escapeSqlString
import com.example.musicapp.domain.joinToSb
import com.example.musicapp.domain.logic.pure.query.SelectListDbQuery
import com.example.musicapp.domain.logic.pure.sql.Basic
import com.example.musicapp.domain.surroundedBy

object SongsQuery {
    private fun searchByTextCondition(
        text: String,
    ) : CharSequence {
        if (text.isEmpty()) {
            return ""
        }
        return buildString {
            append(Tables.GenTemplate.main)
            append(" LIKE ")
            surroundedBy("'") {
                append(text.escapeSqlString())
                append('%')
            }
            append(" OR ")
            append(Tables.GenTemplate.sub)
            append(" LIKE ")
            surroundedBy("'") {
                append(text.escapeSqlString())
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
            append("SELECT ")
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