package com.example.musicapp.domain.logic.impure.iface.db

import com.example.musicapp.domain.logic.pure.query.SqlDbQuery

interface DbQueryInterpreter {
    fun <T> executeOrFail(query: SqlDbQuery<T>): Result<T>
    fun <T> execute(query: SqlDbQuery<T>): T
}