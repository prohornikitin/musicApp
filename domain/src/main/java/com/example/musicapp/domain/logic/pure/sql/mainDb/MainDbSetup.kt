package com.example.musicapp.domain.logic.pure.sql.mainDb

import com.example.musicapp.domain.data.SongCardText
import com.example.musicapp.domain.data.ConfigKey
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.escapeSqlString
import com.example.musicapp.domain.joinToSb
import com.example.musicapp.domain.logic.pure.query.Arg
import com.example.musicapp.domain.logic.pure.query.InsertDbQuery
import com.example.musicapp.domain.logic.pure.query.SelectListDbQuery
import com.example.musicapp.domain.logic.pure.query.SelectOneDbQuery
import com.example.musicapp.domain.logic.pure.query.SimpleWriteDbQuery
import com.example.musicapp.domain.logic.pure.sql.DbSetupSql
import com.example.musicapp.domain.logic.pure.sql.Table
import com.example.musicapp.domain.surroundedBy

object MainDbSetup : DbSetupSql {
    override val tables: List<Table> = listOf(
        Tables.Song,
        Tables.IconFile,
        Tables.Meta,
        Tables.GenTemplate,
        Tables.KvConfig,
        Tables.MetaKeyMappings,
    )

    override fun init(): List<SimpleWriteDbQuery> = super.init() + listOf(
        Config.setValue(ConfigKey.MAIN_TEMPLATE, "#TITLE"),
        Config.setValue(ConfigKey.SUB_TEMPLATE, "#ALBUM"),
        Config.setValue(ConfigKey.META_DELIMITER, "/"),
    )

    val getAllSongIds = SelectListDbQuery<SongId>(
        "SELECT ${Tables.Song.id} FROM ${Tables.Song}",
        { SongId(getLong(0)) }
    )

    private val getFileBySongIdWithoutArgs = SelectOneDbQuery<String>(
        "SELECT ${Tables.Song.musicFilePath} FROM ${Tables.Song} WHERE ${Tables.Song.id}=?",
        { getString(0) },
    )
    fun getFileBySongId(id: SongId) = getFileBySongIdWithoutArgs.withArgs(
        Arg.Companion.of(id.raw)
    )

    fun getFilesPathsBySongId(ids: Collection<SongId>): SelectListDbQuery<Pair<SongId, String>> {
        return SelectListDbQuery(
            if (ids.isEmpty()) {
                ""
            } else buildString {
                append("SELECT ")
                append(Tables.Song.id)
                append(',')
                append(Tables.Song.musicFilePath)
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

    fun getSongCardTextFor(ids: List<SongId>) = SelectListDbQuery<Pair<SongId, SongCardText>>(
        if(ids.isEmpty()) {
            ""
        } else buildString {
            append(
                "SELECT ${Tables.Song.id.fullName}, ${Tables.GenTemplate.main.fullName}, ${Tables.GenTemplate.sub.fullName} " +
                "FROM ${Tables.Song} " +
                "JOIN ${Tables.GenTemplate} ON ${Tables.Song.id.fullName}=${Tables.GenTemplate.songId.fullName} " +
                "WHERE ${Tables.Song.id.fullName} IN ("
            )
            ids.joinToSb(this, ",") {
                append(it.raw)
            }
            append(")")
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



    val getIconPathWithoutArg = SelectOneDbQuery(
        "SELECT ${Tables.Song.musicFilePath} ICON_DATA FROM ${Tables.IconFile} " +
        "WHERE ${Tables.Song.iconPath} = ? LIMIT 1",
        { getString(0) }
    )
    fun getIconPath(id: SongId) = getIconPathWithoutArg.withArgs(
        Arg.of(id.raw)
    )

    private val insertSongWithoutArgs = InsertDbQuery(
        "INSERT OR IGNORE INTO ${Tables.Song} (${Tables.Song.musicFilePath}) VALUES (?)"
    )
    fun insertSongFileIfNotExists(filePath: String) = insertSongWithoutArgs.withArgs(
        Arg.of(filePath)
    )

    private val updateSongIconWithoutArgs = SimpleWriteDbQuery(
        "UPDATE ${Tables.Song} SET ${Tables.Song.iconPath}=? WHERE ${Tables.Song.id}=?",
    )
    fun updateSongIconPath(songId: SongId, iconPath: String?) = updateSongIconWithoutArgs.withArgs(
        Arg.of(iconPath),
        Arg.of(songId.raw)
    )
}