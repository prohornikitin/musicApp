package com.example.musicapp.domain.logic.pure.sql.mainDb

import com.example.musicapp.domain.data.ConfigKey
import com.example.musicapp.domain.logic.pure.sql.mainDb.Tables.KvConfig
import com.example.musicapp.domain.logic.pure.query.Arg
import com.example.musicapp.domain.logic.pure.query.SelectOneDbQuery
import com.example.musicapp.domain.logic.pure.query.SimpleWriteDbQuery

object Config {
    private val getValueWithoutArgs = SelectOneDbQuery(
        "SELECT ${KvConfig.value} FROM $KvConfig WHERE ${KvConfig.key} == ? LIMIT 1",
        { getString(0) },
    )
    fun getValue(key: ConfigKey) = getValueWithoutArgs.withArgs(
        Arg.Companion.of(key.raw)
    )

    private val setValueWithoutArgs = SimpleWriteDbQuery(
        "REPLACE INTO $KvConfig (${KvConfig.key}, ${KvConfig.value}) VALUES (?,?)"
    )
    fun setValue(key: ConfigKey, value: String) = setValueWithoutArgs.withArgs(
        Arg.of(key.raw), Arg.of(value)
    )
}