package com.example.musicapp.domain.logic.pure.sql

interface DbSql {
    fun upgrade(oldVersion: Int): List<CharSequence> {
        return emptyList()
    }
}