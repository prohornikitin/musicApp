package afc.musicapp.domain.logic.impure.iface.db

import afc.musicapp.domain.logic.pure.sql.query.SqlDbQuery

interface DbQueryInterpreter {
    suspend fun <T> execute(query: SqlDbQuery<T>): Result<T>
    suspend fun <T> executeOrThrow(query: SqlDbQuery<T>): T = execute(query).getOrThrow()
    suspend fun <T> executeOrDefault(query: SqlDbQuery<T>): T

    enum class TransactionResult {
        COMMIT,
        ROLLBACK
    }

    suspend fun transaction(block: suspend DbQueryInterpreter.() -> TransactionResult): Result<Unit>
}