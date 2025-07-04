package com.example.musicapp.domain.logic.pure.query

sealed interface SqlDbQuery<T>

data class SimpleWriteDbQuery(
    val sql: String,
    val bindArgs: List<Arg> = emptyList(),
) : SqlDbQuery<Unit> {
    fun withArgs(vararg arg: Arg) = SimpleWriteDbQuery(sql, arg.toList())
}


data class InsertDbQuery(
    val sql: String,
    val bindArgs: List<Arg> = emptyList(),
) : SqlDbQuery<Long?> {
    fun withArgs(vararg arg: Arg) = InsertDbQuery(sql, arg.toList())
}


data class SelectListDbQuery<T>(
    val sql: String,
    val rowProcess: SelectedRow.() -> T,
    val bindArgs: List<Arg> = emptyList()
) : SqlDbQuery<List<T>> {
    fun withArgs(vararg arg: Arg) = SelectListDbQuery(sql, rowProcess, arg.toList())
}

data class SelectOneDbQuery<T: Any>(
    val sql: String,
    val rowProcess: SelectedRow.() -> T,
    val bindArgs: List<Arg> = emptyList()
) : SqlDbQuery<T?> {
    fun withArgs(vararg arg: Arg) = SelectOneDbQuery(sql, rowProcess, arg.toList())
}