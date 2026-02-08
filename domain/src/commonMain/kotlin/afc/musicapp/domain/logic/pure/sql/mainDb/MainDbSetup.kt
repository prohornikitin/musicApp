package afc.musicapp.domain.logic.pure.sql.mainDb

import afc.musicapp.domain.entities.SongCardText
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.escapeSqlString
import afc.musicapp.domain.joinToSb
import afc.musicapp.domain.logic.pure.sql.DbSetupSql
import afc.musicapp.domain.logic.pure.sql.Table
import afc.musicapp.domain.logic.pure.sql.query.Arg
import afc.musicapp.domain.logic.pure.sql.query.InsertDbQuery
import afc.musicapp.domain.logic.pure.sql.query.SelectListDbQuery
import afc.musicapp.domain.logic.pure.sql.query.SelectOneDbQuery
import afc.musicapp.domain.logic.pure.sql.query.SimpleWriteDbQuery
import afc.musicapp.domain.surroundedBy

object MainDbSetup : DbSetupSql {
    override val tables: List<Table> = listOf(
        Tables.Song,
        Tables.Meta,
        Tables.GenTemplate,
        Tables.KvConfig,
        Tables.MetaKeyMappings,
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

    val removeFileFromDbWithoutArgs = SimpleWriteDbQuery(
        "DELETE FROM ${Tables.Song} WHERE ${Tables.Song.id}=?"
    )
    fun removeFileFromDb(id: SongId) = removeFileFromDbWithoutArgs.withArgs(Arg.Companion.of(id.raw))

    fun getSongCardTextFor(ids: List<SongId>) = SelectListDbQuery<Pair<SongId, SongCardText>>(
        if (ids.isEmpty()) {
            ""
        } else buildString {
            append(
                "SELECT ${Tables.GenTemplate.main}, ${Tables.GenTemplate.sub} " +
                        "FROM ${Tables.GenTemplate} " +
                        "WHERE ${Tables.Song.id} IN ("
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
        "SELECT ${Tables.Song.iconPath} FROM ${Tables.Song} " +
                "WHERE ${Tables.Song.iconPath} = ? LIMIT 1",
        { getStringOrNull(0) }
    )
    fun getIconPath(id: SongId): SelectOneDbQuery<String?> = getIconPathWithoutArg.withArgs(
        Arg.Companion.of(id.raw)
    )

    private val insertSongWithoutArgs = InsertDbQuery(
        "INSERT OR IGNORE INTO ${Tables.Song} (${Tables.Song.musicFilePath}) VALUES (?)"
    )
    fun insertSongFileIfNotExists(filePath: String) = insertSongWithoutArgs.withArgs(
        Arg.Companion.of(filePath)
    )

    private val updateSongIconWithoutArgs = SimpleWriteDbQuery(
        "UPDATE ${Tables.Song} SET ${Tables.Song.iconPath}=? WHERE ${Tables.Song.id}=?",
    )
    fun updateSongIconPath(songId: SongId, iconPath: String?) = updateSongIconWithoutArgs.withArgs(
        Arg.Companion.of(iconPath),
        Arg.Companion.of(songId.raw)
    )
}