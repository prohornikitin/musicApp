package afc.musicapp.domain.logic.pure.sql.mainDb

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.logic.pure.sql.query.Arg
import afc.musicapp.domain.logic.pure.sql.query.InsertDbQuery
import afc.musicapp.domain.logic.pure.sql.query.SelectOneDbQuery

object MetaMapping {
    private val getWithoutArgs = SelectOneDbQuery(
        "SELECT ${Tables.MetaKeyMappings.value} FROM ${Tables.MetaKeyMappings} WHERE ${Tables.MetaKeyMappings.key}=? LIMIT 1",
        { MetaKey(getString(1)) },
    )
    fun get(key: String) = getWithoutArgs.withArgs(
        Arg.Companion.of(key),
    )


    private val putWithoutArgs = InsertDbQuery(
        "INSERT OR REPLACE INTO ${Tables.MetaKeyMappings} (${Tables.MetaKeyMappings.key}, ${Tables.MetaKeyMappings.value}) VALUES (?,?)"
    )
    fun put(key: String, map: MetaKey) = putWithoutArgs.withArgs(
        Arg.Companion.of(key), Arg.Companion.of(map.raw),
    )
}