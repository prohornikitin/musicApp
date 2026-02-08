package afc.musicapp.domain.logic.pure.sql

import afc.musicapp.domain.logic.pure.sql.query.SimpleWriteDbQuery

interface DbSetupSql {
    val currentVersion: Int
        get() = 1

    val tables: List<Table>

    fun upgrade(fromVersion: Int): List<SimpleWriteDbQuery> = emptyList()
    fun init(): List<SimpleWriteDbQuery> = tables.map { SimpleWriteDbQuery(it.createSql) }
    fun drop(): List<SimpleWriteDbQuery> = tables.map { SimpleWriteDbQuery(it.dropSql) }
}