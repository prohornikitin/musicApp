package afc.musicapp.data.pure.sql.mainDb

import afc.musicapp.data.impure.iface.db.query.Arg
import afc.musicapp.data.impure.iface.db.query.SelectOneDbQuery
import afc.musicapp.data.impure.iface.db.query.SimpleWriteDbQuery

object Config {
    private val getValueWithoutArgs = SelectOneDbQuery(
        "SELECT ${Tables.KvConfig.value} FROM ${Tables.KvConfig} WHERE ${Tables.KvConfig.key} == ? LIMIT 1",
        { getString(0) },
    )
    fun getValue(key: String) = getValueWithoutArgs.withArgs(
        Arg.of(key)
    )

    private val setValueWithoutArgs = SimpleWriteDbQuery(
        "REPLACE INTO ${Tables.KvConfig} (${Tables.KvConfig.key}, ${Tables.KvConfig.value}) VALUES (?,?)"
    )
    fun setValue(key: String, value: String) = setValueWithoutArgs.withArgs(
        Arg.of(key), Arg.of(value)
    )
}