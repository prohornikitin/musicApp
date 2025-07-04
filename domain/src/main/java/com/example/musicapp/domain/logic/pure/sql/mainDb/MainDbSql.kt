package com.example.musicapp.domain.logic.pure.sql.mainDb

import com.example.config.SongCardText
import com.example.musicapp.domain.data.ConfigKey
import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SearchQuery
import com.example.musicapp.domain.data.SongCardData
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.escapeSqlString
import com.example.musicapp.domain.joinToSb
import com.example.musicapp.domain.logic.pure.query.Arg
import com.example.musicapp.domain.logic.pure.query.SelectListDbQuery
import com.example.musicapp.domain.logic.pure.query.SelectOneDbQuery
import com.example.musicapp.domain.logic.pure.query.SimpleWriteDbQuery
import com.example.musicapp.domain.logic.pure.sql.Basic
import com.example.musicapp.domain.logic.pure.sql.DbSetupSql
import com.example.musicapp.domain.logic.pure.sql.Table
import com.example.musicapp.domain.surroundedBy

object MainDbSql : DbSetupSql {
    override val tables: List<Table> = listOf(
        Tables.Song,
        Tables.IconFile,
        Tables.Meta,
        Tables.GenTemplate,
        Tables.KvConfig,
    )

    override fun init(): List<SimpleWriteDbQuery> = super.init() + listOf(
        setConfigValues(mapOf(
            ConfigKey.MAIN_TEMPLATE to "#TITLE",
            ConfigKey.SUB_TEMPLATE to "#ALBUM",
        )),
    )

    private val getMetaMappingByKeyWithoutArgs = SelectOneDbQuery(
        "SELECT ${Tables.MetaKeyMappings.value} FROM ${Tables.MetaKeyMappings} WHERE ${Tables.MetaKeyMappings.key}=? LIMIT 1",
        { MetaKey(getString(1)) },
    )
    fun getMetaMappingByKey(key: String) =
        getMetaMappingByKeyWithoutArgs.withArgs(
            Arg.Companion.of(key),
        )

    private val getConfigValueWithoutArgs =
        SelectListDbQuery(
            "SELECT ${Tables.KvConfig.key} FROM ${Tables.KvConfig} WHERE ${Tables.KvConfig.key} == ? LIMIT 1",
            { getString(0) },
        )

    fun getConfigValue(key: ConfigKey) = getConfigValueWithoutArgs.withArgs(
        Arg.Companion.of(key.raw)
    )

    fun setConfigValues(keys: Map<ConfigKey, String>) = SimpleWriteDbQuery(
        buildString {
            append("REPLACE INTO ")
            append(Tables.KvConfig)
            append(" (")
            append(Tables.KvConfig.key)
            append(',')
            append(Tables.KvConfig.value)
            append(") VALUES ")
            keys.entries.joinToSb(this, ",") {
                append("(?, ?)")
            }
        },
        keys.entries.flatMap {
            listOf(Arg.Companion.of(it.key.raw), Arg.Companion.of(it.value))
        }
    )

    private fun searchInIdsCondition(
        ids: Collection<SongId>
    ): CharSequence {
        if (ids.isEmpty()) {
            return "FALSE"
        }
        return buildString {
            append(Tables.GenTemplate.songId)
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

    val getAllSongIds = SelectListDbQuery<Long>(
        "SELECT ${Tables.Song.id} FROM ${Tables.Song}",
        { getLong(0) }
    )

    private val getFileBySongIdWithoutArgs = SelectOneDbQuery<String>(
        "SELECT ${Tables.Song.musicFileId} FROM ${Tables.Song} WHERE ${Tables.Song.id}=?",
        { getString(0) },
    )
    fun getFileBySongId(id: SongId) = getFileBySongIdWithoutArgs.withArgs(
        Arg.Companion.of(id.raw)
    )

    fun getFilesPathsBySongId(ids: List<SongId>): SelectListDbQuery<Pair<SongId, String>> {
        return SelectListDbQuery(
            if (ids.isEmpty()) {
                ""
            } else buildString {
                append("SELECT ")
                append(Tables.Song.id)
                append(',')
                append(Tables.Song.musicFileId)
                append(" FROM ")
                append(Tables.Song)
                append(" WHERE ")
                append(Tables.Song.id)
                append(" IN (")
                ids.joinToSb(this, ",") {
                    append(it.raw)
                }
                append(')')
            },
            { Pair(SongId(getLong(0)), getString(1)) },
        )
    }

    fun getMetasForAll(metas: Set<MetaKey>) = SelectListDbQuery<Triple<SongId, MetaKey, String>>(
        if (metas.isEmpty()) {
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
            append(')')
        },
        { Triple(SongId(getLong(0)), MetaKey(getString(1)), getString(2)) }
    )

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
            append(") VALUES (?,?,?)")
            (2..map.entries.size).forEach {
                append(",(?,?,?)")
            }
        },
        map.flatMap {
            listOf(Arg.Companion.of(it.key.raw), Arg.Companion.of(it.value.main), Arg.Companion.of(it.value.sub))
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

    val removeFileFromDbWithoutArgs = SimpleWriteDbQuery(
        "DELETE FROM ${Tables.Song} WHERE ${Tables.Song.id}=?"
    )
    fun removeFileFromDb(id: SongId) = removeFileFromDbWithoutArgs.withArgs(Arg.Companion.of(id.raw))

    fun getSongCardDataFor(ids: List<SongId>) = SelectListDbQuery<SongCardData>(
        buildString {
            append(
                "SELECT ${Tables.Song.id}, ${Tables.GenTemplate.main}, ${Tables.GenTemplate.sub}, ${Tables.IconFile.data} " +
                        "FROM ${Tables.Song} " +
                        "JOIN ${Tables.GenTemplate} ON ${Tables.Song.id}=${Tables.GenTemplate.songId}" +
                        "JOIN ${Tables.IconFile} ON ${Tables.Song.iconId}=${Tables.IconFile.id}" +
                        "WHERE ${Tables.Song.id} IN ("
            )
            ids.joinToSb(this, ",") {
                append(it.raw)
            }
            append(")")
        },
        {
            SongCardData(
                id = SongId(getLong(0)),
                mainText = getString(1),
                bottomText = getString(2),
                iconBitmap = getBlob(3),
            )
        }
    )



    fun getIconFor(id: SongId) =
        "SELECT ${Tables.IconFile.id} ICON_DATA FROM ${Tables.IconFile} " +
        "JOIN ${Tables.Song} ON ${Tables.IconFile.id} = ${Tables.Song.iconId}" +
        "WHERE ${Tables.Song.iconId} = ${id.raw} LIMIT 1"

    fun removeMetaByKeys(id: SongId, keys: List<MetaKey>) = buildString {
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
                surroundedBy("'") {
                    append(it.raw)
                }
            }
            append(")")
        }
    }

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

    val insertIcon = "INSERT OR IGNORE INTO ${Tables.IconFile} (${Tables.IconFile.data}) VALUES (?); "
    val getIconIdByData = "SELECT ${Tables.IconFile.id} FROM ${Tables.IconFile} WHERE ${Tables.IconFile.data}=?"

    private val updateSongIconWithoutArgs = SimpleWriteDbQuery(
        "UPDATE ${Tables.Song} SET ${Tables.Song.iconId}=? WHERE ${Tables.Song.id}=?",
    )
    fun updateSongIcon(songId: SongId, iconId: Long?) = updateSongIconWithoutArgs.withArgs(
        Arg.Companion.of(iconId),
        Arg.Companion.of(songId.raw)
    )
}