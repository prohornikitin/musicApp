package afc.musicapp.domain.logic.pure.sql

class Column(private val table: String, val column: String) {
    override fun toString() = column
    val fullName = "$table.$column"
}

abstract class Table(val table: String) {
    fun col(column: String) = Column(table, column)
    override fun toString() = table
    abstract val createSql: String
    val dropSql = "DROP TABLE IF EXISTS $table"
}