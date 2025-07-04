package com.example.musicapp.domain.logic.pure.sql

abstract class Table(val table: String) {
    fun col(column: String) = "${table}.$column"
    override fun toString() = table
    abstract val createSql: String
    val dropSql = "DROP TABLE IF EXISTS $table"
}