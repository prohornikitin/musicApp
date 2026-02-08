package afc.musicapp.domain.logic.pure.sql.mainDb

import afc.musicapp.domain.logic.pure.sql.query.Arg
import afc.musicapp.domain.logic.pure.sql.query.SelectOneDbQuery
import afc.musicapp.domain.logic.pure.sql.query.SimpleWriteDbQuery

object Config {
    private val getValueWithoutArgs = SelectOneDbQuery(
        "SELECT ${Tables.KvConfig.value} FROM ${Tables.KvConfig} WHERE ${Tables.KvConfig.key} == ? LIMIT 1",
        { getString(0) },
    )
    fun getValue(key: String) = getValueWithoutArgs.withArgs(
        Arg.Companion.of(key)
    )

    private val setValueWithoutArgs = SimpleWriteDbQuery(
        "REPLACE INTO ${Tables.KvConfig} (${Tables.KvConfig.key}, ${Tables.KvConfig.value}) VALUES (?,?)"
    )
    fun setValue(key: String, value: String) = setValueWithoutArgs.withArgs(
        Arg.Companion.of(key), Arg.Companion.of(value)
    )
}