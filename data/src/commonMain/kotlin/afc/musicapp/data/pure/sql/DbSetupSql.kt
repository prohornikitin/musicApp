package afc.musicapp.data.pure.sql

import afc.musicapp.data.impure.iface.db.query.SimpleWriteDbQuery

interface DbSetupSql {
    val currentVersion: Int

    val tables: List<Table>

    fun upgrade(fromVersion: Int): List<SimpleWriteDbQuery> = emptyList()
    fun init(): List<SimpleWriteDbQuery> = tables.map { SimpleWriteDbQuery(it.createSql) }
    fun drop(): List<SimpleWriteDbQuery> = tables.map { SimpleWriteDbQuery(it.dropSql) }
}