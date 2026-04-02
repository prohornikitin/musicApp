package afc.musicapp.data.pure.sql.mainDb

import afc.musicapp.domain.entities.SongId
import afc.musicapp.data.impure.iface.db.query.Arg
import afc.musicapp.data.impure.iface.db.query.InsertDbQuery
import afc.musicapp.data.impure.iface.db.query.SelectListDbQuery
import afc.musicapp.data.impure.iface.db.query.SelectOneDbQuery
import afc.musicapp.data.impure.iface.db.query.SimpleWriteDbQuery
import afc.musicapp.data.pure.sql.Basic.inClause

object Songs {
    val countAll = SelectOneDbQuery<Long>(
        "SELECT COUNT(1) FROM ${Tables.Song}",
        { getLong(0) },
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
                inClause(ids) {
                    append(it.raw)
                }
            },
            { Pair(SongId(getLong(0)), getString(1)) },
        )
    }


    private val getIconPathWithoutArg = SelectOneDbQuery(
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


    private val getSongIdByFileWithoutArgs = InsertDbQuery(
        "SELECT (${Tables.Song.id}) FROM ${Tables.Song} WHERE ${Tables.Song.musicFilePath}=?"
    )

    fun getSongIdByFile(path: String) = getSongIdByFileWithoutArgs.withArgs(
        Arg.Companion.of(path)
    )


    private val updateSongIconWithoutArgs = SimpleWriteDbQuery(
        "UPDATE ${Tables.Song} SET ${Tables.Song.iconPath}=? WHERE ${Tables.Song.id}=?",
    )

    fun updateSongIconPath(songId: SongId, iconPath: String?) = updateSongIconWithoutArgs.withArgs(
        Arg.Companion.of(iconPath),
        Arg.Companion.of(songId.raw)
    )


    private val removeFileFromDbWithoutArgs = SimpleWriteDbQuery(
        "DELETE FROM ${Tables.Song} WHERE ${Tables.Song.id}=?"
    )

    fun removeFileFromDb(id: SongId) = removeFileFromDbWithoutArgs.withArgs(Arg.Companion.of(id.raw))
}