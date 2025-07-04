package com.example.musicapp.domain.logic.pure.sql

import com.example.musicapp.domain.logic.pure.query.SimpleWriteDbQuery

interface DbSetupSql {
    val currentVersion: Int
        get() = 0

    val tables: List<Table>

    fun upgrade(fromVersion: Int): List<SimpleWriteDbQuery> = emptyList()
    fun init(): List<SimpleWriteDbQuery> = tables.map { SimpleWriteDbQuery(it.createSql) }
    fun drop(): List<SimpleWriteDbQuery> = tables.map { SimpleWriteDbQuery(it.dropSql) }
}