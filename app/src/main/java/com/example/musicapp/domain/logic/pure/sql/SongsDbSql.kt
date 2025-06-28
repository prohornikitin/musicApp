package com.example.musicapp.domain.logic.pure.sql

import com.example.config.SongCardText
import com.example.musicapp.domain.joinToSb
import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SearchQuery
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.escapeSingleQuotesSql
import com.example.musicapp.domain.logic.pure.sql.Basic.and
import com.example.musicapp.domain.surroundedBy

object SongsDbSql : DbSql {
    const val DATABASE_VERSION = 1


    const val TABLE_SONG_FILE = "song_file"
    const val SONG_FILE_FILE = "file"
    const val SONG_FILE_SONG = "song_id"

    const val TABLE_SONG_META = "song_meta"
    const val SONG_META_KEY = "key"
    const val SONG_META_VALUE = "value"
    const val SONG_META_SONG = "song_id"

    const val TABLE_GEN_TEMPLATE = "gen_template"
    const val GEN_TEMPLATE_MAIN = "main"
    const val GEN_TEMPLATE_SUB = "sub"
    const val GEN_TEMPLATE_SONG = "song_id"

    const val TABLE_SONG_ICON = "song_icon"
    const val SONG_ICON_SONG = "song_id"
    const val SONG_ICON_ICON = "icon"


    const val CREATE_FILE_TABLE =
        "CREATE TABLE $TABLE_SONG_FILE(" +
            "$SONG_FILE_SONG INTEGER PRIMARY KEY," +
            "$SONG_FILE_FILE TEXT NOT NULL UNIQUE" +
        ")"

    const val CREATE_ICON_TABLE =
        "CREATE TABLE $TABLE_SONG_ICON(" +
            "$SONG_ICON_SONG INTEGER PRIMARY KEY," +
            "$SONG_ICON_ICON BLOB NOT NULL" +
        ")"

    const val CREATE_META_TABLE =
        "CREATE TABLE $TABLE_SONG_META(" +
            "$SONG_META_SONG INTEGER NOT NULL," +
            "$SONG_META_KEY TEXT NOT NULL," +
            "$SONG_META_VALUE TEXT NOT NULL," +
            "PRIMARY KEY($SONG_META_SONG, $SONG_META_KEY)" +
        ")"

    const val CREATE_GEN_TEMPLATE_TABLE =
        "CREATE TABLE $TABLE_GEN_TEMPLATE(" +
            "$GEN_TEMPLATE_SONG INTEGER NOT NULL," +
            "$GEN_TEMPLATE_MAIN TEXT NOT NULL," +
            "$GEN_TEMPLATE_SUB TEXT NOT NULL," +
            "PRIMARY KEY($GEN_TEMPLATE_SONG)" +
        ")"



    private fun searchInIdsCondition(
        ids: Collection<SongId>
    ): CharSequence {
        if (ids.isEmpty()) {
            return "FALSE"
        }
        return StringBuilder().apply {
            append(GEN_TEMPLATE_SONG)
            append(" IN (")
            ids.joinToSb(this, ",") {
                append(it.raw)
            }
            append(')')
        }
    }

    private fun searchByTextCondition(
        text: String,
    ) : CharSequence {
        if (text.isEmpty()) {
            return ""
        }
        return StringBuilder().apply {
            append('(')
            append(GEN_TEMPLATE_MAIN)
            append(" LIKE ")
            surroundedBy("'") {
                append(text.escapeSingleQuotesSql())
                append('%')
            }
            append(" OR ")
            append(GEN_TEMPLATE_SUB)
            append(" LIKE ")
            surroundedBy("'") {
                append(text)
                append('%')
            }
            append(')')
        }
    }

    private fun searchByMetaPrefixes(prefixes: Map<MetaKey, String>) : CharSequence {
        if(prefixes.isEmpty()) {
            return ""
        }
        return StringBuilder().apply {
            append(GEN_TEMPLATE_SONG)
            append(" IN (")
            append("SELECT ")
            append(SONG_META_SONG)
            append(" FROM ")
            append(TABLE_SONG_META)

            append(" WHERE ")
            prefixes.entries.joinToSb(this, ") OR (") {
                append(SONG_META_KEY)
                append('=')
                surroundedBy("'") {
                    append(it.key.raw)
                }
                append(" AND ")
                append(SONG_META_VALUE)
                append(" LIKE ")
                surroundedBy("'") {
                    append(it.value)
                    append('%')
                }
            }
            append(" GROUP BY ")
            append(SONG_META_SONG)
            append(" HAVING ")
            append("COUNT(")
            append(SONG_META_SONG)
            append(")=")
            append(prefixes.size)
            append(')')
        }
    }

    fun search(initialList: Collection<SongId>, query: SearchQuery): String {
        if (initialList.isEmpty()) {
            return ""
        }
        return buildString {
            append("SELECT ")
            append(GEN_TEMPLATE_SONG)
            append(" FROM ")
            append(TABLE_GEN_TEMPLATE)
            append(" WHERE ")
            append(
                Basic.andMany(
                    listOf(
                        searchInIdsCondition(initialList),
                        searchByTextCondition(query.plain),
                        searchByMetaPrefixes(query.metaPrefixes),
                    )
                )
            )
        }
    }

    fun replaceMetas(id: SongId, meta: Map<MetaKey, String>) = buildString {
        if(meta.isEmpty()) {
            return@buildString
        }
        append("REPLACE INTO ")
        append(TABLE_SONG_META)
        append(" (")
        append(SONG_META_SONG)
        append(',')
        append(SONG_META_KEY)
        append(',')
        append(SONG_META_VALUE)
        append(") VALUES ")
        meta.entries.joinToSb(this, ",") {
            append('(')
            append(id.raw)
            append(',')
            surroundedBy("'") {
                append(it.key.raw)
            }
            append(',')
            surroundedBy("'") {
                append(it.value.replace("'", "''"))
            }
            append(')')
        }
    }

    const val GET_ALL_SONG_IDS = "SELECT $SONG_FILE_SONG FROM $TABLE_SONG_FILE"

    fun getFileBySongId(id: SongId) = buildString {
        append("SELECT ")
        append(SONG_FILE_FILE)
        append(" FROM ")
        append(TABLE_SONG_FILE)
        append(" WHERE ")
        append(SONG_FILE_SONG)
        append('=')
        append(id.raw)
    }

    fun getFilesBySongId(ids: List<SongId>): String {
        if (ids.isEmpty()) {
            return ""
        }
        return buildString {
            append("SELECT ")
            append(SONG_FILE_SONG)
            append(',')
            append(SONG_FILE_FILE)
            append(" FROM ")
            append(TABLE_SONG_FILE)
            append(" WHERE ")
            append(SONG_FILE_SONG)
            append(" IN (")
            ids.joinToSb(this, ",") {
                append(it.raw)
            }
            append(')')
        }
    }

    fun getMetasForAll(metas: Set<MetaKey>): String {
        if (metas.isEmpty()) {
            return ""
        }
        return buildString {
            append("SELECT ")
            append(SONG_META_SONG)
            append(',')
            append(SONG_META_KEY)
            append(',')
            append(SONG_META_VALUE)
            append(" FROM ")
            append(TABLE_SONG_META)
            append(" WHERE ")
            append(SONG_META_KEY)
            append(" IN (")
            metas.joinToSb(this, ",") {
                surroundedBy("'") {
                    append(it.raw)
                }
            }
            append(')')
        }
    }

    fun updateGeneratedTemplates(map: Map<SongId, SongCardText>): String {
        if (map.isEmpty()) {
            return ""
        }
        return buildString {
            append("REPLACE INTO ")
            append(TABLE_GEN_TEMPLATE)
            append(" (")
            append(GEN_TEMPLATE_SONG)
            append(',')
            append(GEN_TEMPLATE_MAIN)
            append(',')
            append(GEN_TEMPLATE_SUB)
            append(") VALUES ")
            map.entries.joinToSb(this, ",") {
                append('(')
                append(it.key.raw)
                append(',')
                surroundedBy("'") {
                    append(it.value.main)
                }
                append(',')
                surroundedBy("'") {
                    append(it.value.sub)
                }
                append(')')
            }
        }
    }

    fun insertNewTemplate(id: SongId, main: String, sub: String) = buildString {
        append("INSERT INTO ")
        append(TABLE_GEN_TEMPLATE)
        append(" (")
        append(GEN_TEMPLATE_SONG)
        append(',')
        append(GEN_TEMPLATE_MAIN)
        append(',')
        append(GEN_TEMPLATE_SUB)
        append(") VALUES (")
        append(id.raw)
        append(',')
        surroundedBy("'") {
            append(main.replace("'", "''"))
        }
        append(',')
        surroundedBy("'") {
            append(sub.replace("'", "''"))
        }
        append(')')
    }

    fun removeFileFromDb(id: SongId) = buildString {
        append("DELETE FROM ")
        append(TABLE_SONG_FILE)
        append(" WHERE ")
        append(SONG_FILE_SONG)
        append('=')
        append(id.raw)
    }

    fun getTemplateTexts(id: SongId) = buildString {
        append("SELECT ")
        append(GEN_TEMPLATE_MAIN)
        append(',')
        append(GEN_TEMPLATE_SUB)
        append(" FROM ")
        append(TABLE_GEN_TEMPLATE)
        append(" WHERE ")
        append(GEN_TEMPLATE_SONG)
        append('=')
        append(id.raw)
    }



    fun getIconFor(id: SongId) = buildString {
        append("SELECT ")
        append(SONG_ICON_ICON)
        append(" FROM ")
        append(TABLE_SONG_ICON)
        append(" WHERE ")
        append(SONG_ICON_SONG)
        append('=')
        append(id.raw)
    }

    fun removeAllMeta(id: SongId) = buildString {
        append("DELETE FROM ")
        append(TABLE_SONG_META)
        append(" WHERE ")
        append(SONG_META_SONG)
        append('=')
        append(id.raw)
    }

    fun removeMeta(id: SongId, keys: List<MetaKey>) = buildString {
        append("DELETE FROM ")
        append(TABLE_SONG_META)
        append(" WHERE ")
        append(SONG_META_SONG)
        append('=')
        append(id.raw)
        append(" AND ")
        append(SONG_META_KEY)
        append(" IN (")
        keys.forEach {
            surroundedBy("'") {
                append(it.raw)
            }
        }
        append(")")
    }

    val insertIcon = buildString {
        append("REPLACE INTO ")
        append(TABLE_SONG_ICON)
        append(" (")
        append(SONG_ICON_SONG)
        append(',')
        append(SONG_ICON_ICON)
        append(") VALUES (?,?)")
    }
}