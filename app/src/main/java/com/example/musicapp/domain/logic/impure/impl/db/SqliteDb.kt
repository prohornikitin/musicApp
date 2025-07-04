package com.example.musicapp.domain.logic.impure.impl.db

import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import android.database.sqlite.SQLiteStatement
import com.example.musicapp.domain.logic.impure.impl.logger.Logger
import com.example.musicapp.domain.logic.impure.iface.MemoryCache
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.pure.logger.withClassTag
import com.example.musicapp.domain.logic.pure.query.InsertDbQuery
import com.example.musicapp.domain.logic.pure.query.SelectListDbQuery
import com.example.musicapp.domain.logic.pure.query.SelectOneDbQuery
import com.example.musicapp.domain.logic.pure.query.SimpleWriteDbQuery
import com.example.musicapp.domain.logic.pure.query.SqlDbQuery
import com.example.musicapp.domain.logic.pure.sql.mainDb.MainDbSql
import javax.inject.Inject


abstract class SqliteDb @Inject constructor(
    context: Context,
    db: String,
    val sql: MainDbSql,
    logger: Logger,
) : SQLiteOpenHelper(context, db, null, sql.currentVersion), DbQueryInterpreter {

    private val logger = logger.withClassTag(this)
    init {
        MemoryCache.add {
            this.close()
        }
    }

    override fun onCreate(db: SQLiteDatabase?) {
        sql.init().forEach {
            executeWithDbOverride(it, db)
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        sql.upgrade(oldVersion).forEach {
            executeWithDbOverride(it, db)
        }
    }

    private fun <T> executeWithDbOverride(query: SqlDbQuery<T>, overrideDb: SQLiteDatabase? = null): T {
        @Suppress("UNCHECKED_CAST")
        return when(query) {
            is SelectListDbQuery<*> -> selectMany(query, overrideDb).onFailure(logger::error).getOrDefault(emptyList())
            is SelectOneDbQuery<*> -> selectOne(query, overrideDb).onFailure(logger::error).getOrNull()
            is InsertDbQuery -> insert(query, overrideDb).onFailure(logger::error).getOrNull()
            is SimpleWriteDbQuery -> executeSimple(query, overrideDb).onFailure(logger::error).getOrDefault(Unit)
        } as T
    }

    override fun <T> execute(query: SqlDbQuery<T>) = executeWithDbOverride(query)

    private fun <T> selectMany(query: SelectListDbQuery<T>, overrideDb: SQLiteDatabase?): Result<List<T>> = runCatching {
        val db = overrideDb ?: readableDatabase
        if(query.sql.isEmpty()) {
            return@runCatching emptyList()
        }
        val cursorFactory = CustomSelectCursorFactory(query.bindArgs)
        var cursor: Cursor? = null
        try {
            cursor = NullHack.callSelect(db, cursorFactory, query.sql.toString())
            val rowWrapper = CursorRowWrapper(cursor)
            val entities = mutableListOf<T>()
            while (cursor.moveToNext()) {
                entities.add(query.rowProcess(rowWrapper))
            }
            return@runCatching entities
        } finally {
            cursor?.close()
        }
    }

    private fun <T: Any> selectOne(query: SelectOneDbQuery<T>, overrideDb: SQLiteDatabase?): Result<T?> = runCatching {
        val db = overrideDb ?: readableDatabase
        if(query.sql.isEmpty()) {
            return@runCatching null
        }
        val cursorFactory = CustomSelectCursorFactory(query.bindArgs)
        var cursor: Cursor? = null
        try {
            cursor = NullHack.callSelect(db, cursorFactory, query.sql.toString())
            val rowWrapper = CursorRowWrapper(cursor)
            assert(cursor.count <= 1)
            return@runCatching if(cursor.count == 1) {
                query.rowProcess(rowWrapper)
            } else {
                null
            }
        } finally {
            cursor?.close()
        }
    }

    private fun insert(query: InsertDbQuery, overrideDb: SQLiteDatabase?): Result<Long?> = runCatching {
        val db = overrideDb ?: writableDatabase
        if(query.sql.isEmpty()) {
            return@runCatching null
        }
        var statement: SQLiteStatement? = null
        try {
            statement = db.compileStatement(query.sql.toString())
            statement.bindAll(query.bindArgs)
            val rowId = statement.executeInsert()
            return@runCatching if (rowId == -1L) {
                null
            } else {
                rowId
            }
        } finally {
            statement?.close()
        }
    }

    private fun executeSimple(query: SimpleWriteDbQuery, overrideDb: SQLiteDatabase?): Result<Unit> = runCatching {
        val db = overrideDb ?: writableDatabase
        if(query.sql.isEmpty()) {
            return@runCatching
        }
        var statement: SQLiteStatement? = null
        try {
            statement = db.compileStatement(query.sql.toString())
            statement?.bindAll(query.bindArgs)
            statement?.execute()
        } finally {
            statement?.close()
        }
    }
}