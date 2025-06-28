package com.example.musicapp.domain.logic.impure.impl.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import android.os.Environment
import com.example.musicapp.domain.data.ConfigKey
import com.example.musicapp.domain.logic.impure.iface.storage.read.Config
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.ConfigDbEdit
import com.example.musicapp.domain.logic.pure.sql.KvConfigDbSql
import com.example.musicapp.domain.logic.pure.sql.KvConfigDbSql.CREATE_KV_CONFIG_TABLE
import com.example.musicapp.domain.logic.pure.sql.KvConfigDbSql.upgrade
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.DATABASE_VERSION

class ConfigDb(context: Context) :
    SqliteDb(context, "kvConfig.db", DATABASE_VERSION),
    Config,
    ConfigDbEdit
{
    companion object {
        private var instance: ConfigDb? = null

        fun getInstance(context: Context) = synchronized(this) {
            if (instance == null) {
                instance = ConfigDb(context)
            }
            return@synchronized instance!!
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        db?.run {
            execSQL(CREATE_KV_CONFIG_TABLE)
            execSQL(KvConfigDbSql.setConfigValues(mapOf(
                ConfigKey.SCAN_DIRECTORIES to "[${Environment.getExternalStoragePublicDirectory(Environment.DIRECTORY_MUSIC)}]",
                ConfigKey.MAIN_TEMPLATE to "#TITLE",
                ConfigKey.SUB_TEMPLATE to "#ALBUM",
            )))
        }
    }

    override fun onUpgrade(
        db: SQLiteDatabase?,
        oldVersion: Int,
        newVersion: Int
    ) {
        upgrade(oldVersion).forEach {
            db?.execSQL(it.toString())
        }
    }

//    override fun updateTemplates(main: Template, sub: Template) {
//        writableDatabase.execSQL(setConfigTemplates(main.toString(), sub.toString()))
//    }
//
//    override fun getTemplates(): SongCardTemplate {
//        val values = selectMultiple(getConfigTemplates) {
//            Pair(getString(0), getString(1))
//        }.toMap()
//        return SongCardTemplate(
//            values[KV_CONFIG_KEY_MAIN_TEMPLATE] ?: "",
//            values[KV_CONFIG_KEY_SUB_TEMPLATE] ?: ""
//        )
//    }

    override fun read(key: ConfigKey): String? {
        return selectOne(KvConfigDbSql.getConfigValue(key)) {
            getString(0)
        }
    }

    override fun read(keys: List<ConfigKey>): Map<ConfigKey, String> {
        return selectMultiple(KvConfigDbSql.getConfigValues(keys)) {
            Pair(ConfigKey.findByKey(getString(0))!!, getString(1))
        }.toMap()
    }

    override fun update(key: ConfigKey, value: String) {
        update(mapOf(key to value))
    }

    override fun update(values: Map<ConfigKey, String>) {
        writableDatabase?.execSQL(KvConfigDbSql.setConfigValues(values))
    }
}