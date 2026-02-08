package afc.musicapp.domain.logic.pure.sql.query

interface SelectedRow {
    fun isNull(column: Int): Boolean

    fun getBlob(column: Int): ByteArray
    fun getString(column: Int): String

    fun getLong(column: Int): Long
    fun getInt(column: Int): Int
    fun getShort(column: Int): Short

    fun getFloat(column: Int): Float
    fun getDouble(column: Int): Double

    fun getBlobOrNull(column: Int): ByteArray? = if(isNull(column)) null else getBlob(column) 
    fun getStringOrNull(column: Int): String? = if(isNull(column)) null else getString(column)

    fun getLongOrNull(column: Int): Long? = if(isNull(column)) null else getLong(column)
    fun getIntOrNull(column: Int): Int? = if(isNull(column)) null else getInt(column)
    fun getShortOrNull(column: Int): Short? = if(isNull(column)) null else getShort(column)

    fun getFloatOrNull(column: Int): Float? = if(isNull(column)) null else getFloat(column)
    fun getDoubleOrNull(column: Int): Double? = if(isNull(column)) null else getDouble(column)
}