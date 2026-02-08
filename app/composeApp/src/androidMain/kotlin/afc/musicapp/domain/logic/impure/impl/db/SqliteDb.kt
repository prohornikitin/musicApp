package afc.musicapp.domain.logic.impure.impl.db

import android.content.Context
import android.database.Cursor
import afc.musicapp.domain.logic.impure.iface.Dispatchers
import afc.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import afc.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter.TransactionResult
import afc.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter.TransactionResult.*
import afc.musicapp.domain.logic.impure.impl.logger.Logger
import afc.musicapp.domain.logic.pure.logger.withClassTag
import afc.musicapp.domain.logic.pure.sql.query.InsertDbQuery
import afc.musicapp.domain.logic.pure.sql.query.SelectListDbQuery
import afc.musicapp.domain.logic.pure.sql.query.SelectOneDbQuery
import afc.musicapp.domain.logic.pure.sql.query.SimpleWriteDbQuery
import afc.musicapp.domain.logic.pure.sql.query.SqlDbQuery
import afc.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import com.mayakapps.rwmutex.ReadWriteMutex
import com.mayakapps.rwmutex.withReadLock
import com.mayakapps.rwmutex.withWriteLock
import io.requery.android.database.sqlite.SQLiteDatabase
import io.requery.android.database.sqlite.SQLiteOpenHelper
import io.requery.android.database.sqlite.SQLiteStatement
import kotlinx.coroutines.withContext


abstract class SqliteDb(
    context: Context,
    db: String,
    val sql: MainDbSetup,
    logger: Logger,
    private val dispatchers: Dispatchers,
) : SQLiteOpenHelper(context, db, null, sql.currentVersion), DbQueryInterpreter {
    private val logger = logger.withClassTag(this)
    private val transactionMutex = ReadWriteMutex()

    override fun onCreate(db: SQLiteDatabase?) {
        sql.init().forEach {
            innerExecute(it, db).getOrThrow()
        }
    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        sql.upgrade(oldVersion).forEach {
            innerExecute(it, db).getOrThrow()
        }
    }

    override suspend fun <T> executeOrDefault(query: SqlDbQuery<T>): T = withContext(dispatchers.io) {
        transactionMutex.withReadLock {
            innerExecuteOrDefault(query)
        }
    }
    override suspend fun <T> execute(query: SqlDbQuery<T>): Result<T> = withContext(dispatchers.io) {
        transactionMutex.withReadLock {
            innerExecute(query)
        }
    }

    private fun <T> innerExecute(
        query: SqlDbQuery<T>,
        overrideDb: SQLiteDatabase? = null
    ): Result<T> {
        @Suppress("UNCHECKED_CAST")
        return (when(query) {
            is SelectListDbQuery<*> -> selectMany(query, overrideDb)
            is SelectOneDbQuery<*> -> selectOne(query, overrideDb)
            is InsertDbQuery -> insert(query, overrideDb)
            is SimpleWriteDbQuery -> simpleWrite(query, overrideDb)
        } as Result<T>).also {
            logger.debug { "Executed $query with result $it" }
        }
    }

    private fun <T> innerExecuteOrDefault(
        query: SqlDbQuery<T>,
        overrideDb: SQLiteDatabase? = null
    ): T {
        @Suppress("UNCHECKED_CAST")
        return (when(query) {
            is SelectListDbQuery<*> -> selectMany(query, overrideDb).onFailure(logger::error).getOrDefault(emptyList())
            is SelectOneDbQuery<*> -> selectOne(query, overrideDb).onFailure(logger::error).getOrNull()
            is InsertDbQuery -> insert(query, overrideDb).onFailure(logger::error).getOrNull()
            is SimpleWriteDbQuery -> simpleWrite(query, overrideDb).onFailure(logger::error).getOrDefault(Unit)
        } as T).also {
            logger.debug { "Executed $query with result $it" }
        }
    }

    private fun <T> selectMany(query: SelectListDbQuery<T>, overrideDb: SQLiteDatabase?): Result<List<T>> = runCatching {
        val db = overrideDb ?: readableDatabase
        if(query.sql.isEmpty()) {
            return@runCatching emptyList()
        }
        val cursorFactory = CustomSelectCursorFactory(query.bindArgs)
        var cursor: Cursor? = null
        try {
            cursor = db.rawQueryWithFactory(cursorFactory, query.sql, null, null)
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

    private fun <T> selectOne(query: SelectOneDbQuery<T>, overrideDb: SQLiteDatabase?): Result<T?> = runCatching {
        if(query.sql.isEmpty()) {
            return@runCatching null
        }
        var cursor: Cursor? = null
        try {
            val cursorFactory = CustomSelectCursorFactory(query.bindArgs)
            val db = overrideDb ?: readableDatabase
            cursor = db.rawQueryWithFactory(cursorFactory, query.sql, null, null)
            val count = cursor.count
            assert(count <= 1)
            return@runCatching if(count == 1) {
                cursor.moveToFirst()
                query.rowProcess(CursorRowWrapper(cursor))
            } else {
                null
            }
        } finally {
            cursor?.close()
        }
    }

    private fun insert(query: InsertDbQuery, overrideDb: SQLiteDatabase?): Result<Long?> = runCatching {
        if (query.sql.isEmpty()) {
            return@runCatching null
        }
        var statement: SQLiteStatement? = null
        try {
            val db = overrideDb ?: writableDatabase
            statement = db.compileStatement(query.sql)
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

    private fun simpleWrite(query: SimpleWriteDbQuery, overrideDb: SQLiteDatabase?): Result<Unit> = runCatching {
        if(query.sql.isEmpty()) {
            return@runCatching
        }
        var statement: SQLiteStatement? = null
        try {
            val db = overrideDb ?: writableDatabase
            statement = db.compileStatement(query.sql)
            statement.bindAll(query.bindArgs)
            statement.execute()
        } finally {
            statement?.close()
        }
    }

    private class InnerTransactionFailed() : Exception()

    private val dbInTransaction = object : DbQueryInterpreter {
        var dbEntity: SQLiteDatabase? = null

        override suspend fun <T> execute(query: SqlDbQuery<T>) = this@SqliteDb.innerExecute(query, dbEntity)
        override suspend fun <T> executeOrDefault(query: SqlDbQuery<T>) = this@SqliteDb.innerExecuteOrDefault(query, dbEntity)
        override suspend fun transaction(block: suspend DbQueryInterpreter.() -> TransactionResult): Result<Unit> {
            return when(block()) {
                COMMIT -> Result.success(Unit)
                ROLLBACK -> throw InnerTransactionFailed()
            }
        }
    }

    override suspend fun transaction(block: suspend DbQueryInterpreter.() -> TransactionResult): Result<Unit> = runCatching {
        transactionMutex.withWriteLock {
            writableDatabase.apply {
                beginTransaction()
                try {
                    dbInTransaction.dbEntity = this
                    if (dbInTransaction.block() == COMMIT) {
                        setTransactionSuccessful()
                    }
                } finally {
                    endTransaction()
                }
            }
        }
    }
}