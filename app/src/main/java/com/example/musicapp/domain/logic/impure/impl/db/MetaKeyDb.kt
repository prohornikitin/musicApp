package com.example.musicapp.domain.logic.impure.impl.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.musicapp.domain.logic.impure.iface.storage.read.MetaKeyMapping
import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.logic.pure.sql.MetaKeyDbSql.CREATE_TABLE_META
import com.example.musicapp.domain.logic.pure.sql.MetaKeyDbSql.GET_ALL_MAPPINGS
import com.example.musicapp.domain.logic.pure.sql.SongsDbSql.DATABASE_VERSION


class MetaKeyDb(context: Context) :
    SqliteDb(context, "metaKey", DATABASE_VERSION),
    MetaKeyMapping
{
    companion object {
        private var instance: MetaKeyDb? = null

        fun getInstance(context: Context) = synchronized(this) {
            if (instance == null) {
                instance = MetaKeyDb(context)
            }
            return@synchronized instance!!
        }
    }


    override fun onCreate(db: SQLiteDatabase?) {
        db?.execSQL(CREATE_TABLE_META)
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {

    }

    override fun getMetaKeyMappings(): Map<String, MetaKey> {
        return selectMultiple(GET_ALL_MAPPINGS) {
            Pair(getString(0), MetaKey(getString(1)))
        }.toMap()
    }
}