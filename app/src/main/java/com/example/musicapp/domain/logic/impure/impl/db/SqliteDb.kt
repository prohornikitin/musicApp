package com.example.musicapp.domain.logic.impure.impl.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteOpenHelper
import com.example.musicapp.domain.logic.impure.iface.Cache


abstract class SqliteDb(context: Context, db: String, version: Int)
    : SQLiteOpenHelper(context, db, null, version), Cache
{
    init {
        Cache.add(this)
    }

    override fun clearTheCache() {
        this.close()
    }

    fun <T> selectMultiple(
        sql: String = "",
        getData: Cursor.() -> T
    ): List<T> {
        if (sql.isEmpty()) {
            return emptyList()
        }
        val cursor = readableDatabase.rawQuery(sql, null)
        return cursor.run {
            val entities = mutableListOf<T>()
            while(moveToNext()) {
                entities.add(getData())
            }
            return entities
        }.also {
            cursor.close()
        }
    }

    fun <T> selectOne(
        sql: String = "",
        getData: Cursor.() -> T?
    ): T? {
        if (sql.isEmpty()) {
            return null
        }
        val cursor = readableDatabase.rawQuery(sql, null)
        return cursor.use {
            if(it.moveToNext()) {
                it.getData()
            } else null
        }
    }
}