package afc.musicapp.data.pure.sql.mainDb

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.data.impure.iface.db.query.Arg
import afc.musicapp.data.impure.iface.db.query.InsertDbQuery
import afc.musicapp.data.impure.iface.db.query.SelectOneDbQuery

object MetaMapping {
    private val getWithoutArgs = SelectOneDbQuery(
        "SELECT ${Tables.MetaKeyMappings.value} FROM ${Tables.MetaKeyMappings} WHERE ${Tables.MetaKeyMappings.key}=? LIMIT 1",
        { MetaKey(getString(1)) },
    )
    fun get(key: String) = getWithoutArgs.withArgs(
        Arg.of(key),
    )


    private val putWithoutArgs = InsertDbQuery(
        "INSERT OR REPLACE INTO ${Tables.MetaKeyMappings} (${Tables.MetaKeyMappings.key}, ${Tables.MetaKeyMappings.value}) VALUES (?,?)"
    )
    fun put(key: String, map: MetaKey) = putWithoutArgs.withArgs(
        Arg.of(key), Arg.of(map.raw),
    )
}