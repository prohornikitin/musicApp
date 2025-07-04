package com.example.musicapp.domain.logic.impure.impl.db

import android.content.Context
import android.database.sqlite.SQLiteDatabase
import com.example.musicapp.domain.logic.impure.impl.logger.Logger
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSql
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSql.upgrade

class MainDb private constructor(
    context: Context,
    logger: Logger
) : SqliteDb(context, "songs.db", MainDbSql, logger) {
    companion object {
        private var instance: MainDb? = null

        fun getInstance(context: Context, logger: Logger) = synchronized(this) {
            if (instance == null) {
                instance = MainDb(context, logger)
            }
            return@synchronized instance!!
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        upgrade(oldVersion).forEach {
            db?.execSQL(it.toString())
        }
    }
}