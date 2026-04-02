package afc.musicapp.data.pure.sql.mainDb

import afc.musicapp.data.pure.sql.Table

object Tables {
    object Song : Table("song") {
        val id = col("song_id")
        val musicFilePath = col("file_path")
        val iconPath = col("icon_path")

        override val createSql: String =
            "CREATE TABLE $this (" +
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
            "CREATE TABLE $this (" +
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
        val mainLowercase = col("main_lower")
        val subLowercase = col("sub_lower")

        override val createSql: String =
            "CREATE TABLE $this (" +
                "$songId INTEGER PRIMARY KEY," +
                "$main TEXT NOT NULL," +
                "$sub TEXT NOT NULL," +
                "$mainLowercase TEXT NOT NULL," +
                "$subLowercase TEXT NOT NULL" +
            ");"
    }

    object KvConfig : Table("kv_config") {
        val key = col("key")
        val value = col("value")

        override val createSql: String =
            "CREATE TABLE $this (" +
                "$key TEXT NOT NULL PRIMARY KEY," +
                "$value TEXT NOT NULL" +
            ");"
    }

    object MetaKeyMappings : Table("meta_key_mappings") {
        val key = col("key")
        val value = col("mapping")

        override val createSql: String =
            "CREATE TABLE $this (" +
                "$key TEXT PRIMARY KEY," +
                "$value TEXT NOT NULL" +
            ");"
    }
}