package com.example.musicapp.domain.logic.pure.sql

import com.example.musicapp.domain.data.ConfigKey
import com.example.musicapp.domain.joinToSb
import com.example.musicapp.domain.surroundedBy

object KvConfigDbSql : DbSql {
    const val DATABASE_VERSION = 1


    const val TABLE_KV_CONFIG = "kv_config"
    const val KV_CONFIG_KEY = "key"
    const val KV_CONFIG_VALUE = "value"

    const val CREATE_KV_CONFIG_TABLE =
        "CREATE TABLE $TABLE_KV_CONFIG(" +
            "$KV_CONFIG_KEY TEXT NOT NULL," +
            "$KV_CONFIG_VALUE TEXT NOT NULL," +
            "PRIMARY KEY($KV_CONFIG_KEY)" +
        ")"

    fun getConfigValue(key: ConfigKey) = buildString {
        append("SELECT ")
        append(KV_CONFIG_VALUE)
        append(" FROM ")
        append(TABLE_KV_CONFIG)
        append(" WHERE ")
        append(KV_CONFIG_KEY)
        append("==")
        surroundedBy("'") {
            append(key.key)
        }
    }

    fun getConfigValues(keys: List<ConfigKey>) = buildString {
        append("SELECT ")
        append(KV_CONFIG_KEY)
        append(',')
        append(KV_CONFIG_VALUE)
        append(" FROM ")
        append(TABLE_KV_CONFIG)
        append(" WHERE ")
        append(KV_CONFIG_KEY)
        append(" IN (")
        keys.joinToSb(this, ",") {
            surroundedBy("'") {
                append(it.key)
            }
        }
        append(')')
    }

    fun setConfigValues(keys: Map<ConfigKey, String>) = buildString {
        append("REPLACE INTO ")
        append(TABLE_KV_CONFIG)
        append(" (")
        append(KV_CONFIG_KEY)
        append(',')
        append(KV_CONFIG_VALUE)
        append(") VALUES ")
        keys.entries.joinToSb(this, ",") {
            append('(')
            surroundedBy("'") {
                append(it.key.key)
            }
            append(',')
            surroundedBy("'") {
                append(it.value)
            }
            append(")")
        }
    }
}