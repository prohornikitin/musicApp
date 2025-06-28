package com.example.musicapp.domain.logic.pure.sql

object MetaKeyDbSql : DbSql {
    const val DATABASE_VERSION = 1

    const val TABLE_MAPPINGS = "meta"
    const val MAPPINGS_ORIGINAL = "key"
    const val MAPPINGS_MAPPING = "mapping"

    const val CREATE_TABLE_META =
        "CREATE TABLE $TABLE_MAPPINGS (" +
            "$MAPPINGS_ORIGINAL TEXT PRIMARY KEY," +
            "$MAPPINGS_MAPPING TEXT NOT NULL" +
        ")"

    const val GET_ALL_MAPPINGS = "SELECT $MAPPINGS_ORIGINAL, $MAPPINGS_MAPPING FROM $TABLE_MAPPINGS"
}