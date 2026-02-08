package afc.musicapp.domain.logic.pure.sql.mainDb

import afc.musicapp.domain.logic.pure.sql.Table

object Tables {
    object Song : Table("song") {
        val id = col("song_id")
        val musicFilePath = col("file_path")
        val iconPath = col("icon_path")

        override val createSql: String =
            "CREATE TABLE $Song (" +
                "$id INTEGER PRIMARY KEY," +
                "$musicFilePath TEXT UNIQUE NOT NULL," +
                "$iconPath TEXT" +
            ");"
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
        val songId = col("song_id")
        val main = col("main")
        val sub = col("sub")

        override val createSql: String =
            "CREATE TABLE $GenTemplate (" +
                "$songId INTEGER PRIMARY KEY," +
                "$main TEXT NOT NULL," +
                "$sub TEXT NOT NULL," +
            ");"
    }

    object GenTemplateSearch : Table("gen_template_search") {
        val songId = col("song_id")
        val main = col("main")
        val sub = col("sub")

        override val createSql: String =
            "CREATE TABLE $GenTemplateSearch (" +
                "$songId INTEGER PRIMARY KEY," +
                "$main TEXT NOT NULL," +
                "$sub TEXT NOT NULL" +
            ");"
    }

    object KvConfig : Table("kv_config") {
        val key = col("key")
        val value = col("value")

        override val createSql: String =
            "CREATE TABLE $KvConfig (" +
                "$key TEXT NOT NULL PRIMARY KEY," +
                "$value TEXT NOT NULL" +
            ");"
    }

    object MetaKeyMappings : Table("meta_key_mappings") {
        val key = col("key")
        val value = col("mapping")

        override val createSql: String =
            "CREATE TABLE $MetaKeyMappings (" +
                "$key TEXT PRIMARY KEY," +
                "$value TEXT NOT NULL" +
            ");"
    }
}