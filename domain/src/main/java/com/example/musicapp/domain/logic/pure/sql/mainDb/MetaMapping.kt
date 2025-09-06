package com.example.musicapp.domain.logic.pure.sql.mainDb

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.logic.pure.query.Arg
import com.example.musicapp.domain.logic.pure.query.InsertDbQuery
import com.example.musicapp.domain.logic.pure.query.SelectOneDbQuery

object MetaMapping {
    private val getWithoutArgs = SelectOneDbQuery(
        "SELECT ${Tables.MetaKeyMappings.value} FROM ${Tables.MetaKeyMappings} WHERE ${Tables.MetaKeyMappings.key}=? LIMIT 1",
        { MetaKey(getString(1)) },
    )
    fun get(key: String) = getWithoutArgs.withArgs(
        Arg.Companion.of(key),
    )


    private val putWithoutArgs = InsertDbQuery(
        "INSERT INTO ${Tables.MetaKeyMappings} (${Tables.MetaKeyMappings.key}, ${Tables.MetaKeyMappings.value}) VALUES (?,?)"
    )
    fun put(key: String, map: MetaKey) = putWithoutArgs.withArgs(
        Arg.of(key), Arg.of(map.raw),
    )
}