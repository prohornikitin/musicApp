package afc.musicapp.data.pure.sql

class Column(table: String, val column: String) {
    override fun toString() = column
    val fullName = "$table.$column"
}

abstract class Table(val table: String) {
    fun col(column: String) = Column(table, column)
    override fun toString() = table
    abstract val createSql: String
    val dropSql = "DROP TABLE IF EXISTS $table"
}