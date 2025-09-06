package com.example.musicapp.domain.logic.pure.sql.mainDb

import com.example.musicapp.domain.logic.pure.sql.Table

object Tables {
    object Song : Table("song") {
        val id = col("song_id")
        val musicFilePath = col("file_path")
        val iconPath = col("icon_path")

        override val createSql: String =
            "CREATE TABLE $Song (" +
                "$id INTEGER PRIMARY KEY," +
                "$musicFilePath TEXT NOT NULL UNIQUE," +
                "$iconPath INTEGER," +
                "FOREIGN KEY ($iconPath) REFERENCES $IconFile (${IconFile.id})" +
            ")"
    }

    object Meta : Table("meta") {
        val id = col("meta_id")
        val songId = col("song_id")
        val key = col("key")
        val value = col("value")

        override val createSql =
            "CREATE TABLE $Meta (" +
                "$id INTEGER PRIMARY KEY," +
                "$songId INTEGER NOT NULL," +
                "$key TEXT NOT NULL," +
                "$value TEXT NOT NULL" +
            "); " +
            "CREATE INDEX ${Meta}_$songId ON $Meta ($songId);"
    }

    object GenTemplate : Table("gen_template") {
        val main = col("main")
        val sub = col("sub")
        val songId = col("song_id")

        override val createSql: String =
            "CREATE TABLE $GenTemplate (" +
                "$songId INTEGER PRIMARY KEY," +
                "$main TEXT NOT NULL," +
                "$sub TEXT NOT NULL" +
            ");"
    }

    object IconFile : Table("icon_file") {
        val id = col("icon_file_id")
        val data = col("data")

        override val createSql: String =
            "CREATE TABLE $IconFile (" +
                "$id INTEGER PRIMARY KEY," +
                "$data BINARY NOT NULL UNIQUE" +
            ")"
    }

    object KvConfig : Table("kv_config") {
        val key = col("key")
        val value = col("value")

        override val createSql: String =
            "CREATE TABLE $KvConfig (" +
                "$key TEXT NOT NULL PRIMARY KEY," +
                "$value TEXT NOT NULL" +
            ")"
    }

    object MetaKeyMappings : Table("meta_key_mappings") {
        val key = col("key")
        val value = col("mapping")

        override val createSql: String =
            "CREATE TABLE $MetaKeyMappings (" +
                "$key TEXT PRIMARY KEY," +
                "$value TEXT NOT NULL" +
            ")"
    }
}