package afc.musicapp.data.impure.impl.db

import afc.musicapp.data.impure.iface.db.query.SelectedRow
import android.database.Cursor

class CursorRowWrapper(val cursor: Cursor) : SelectedRow {
    override fun isNull(column: Int)             = cursor.isNull(column)
    override fun getBlob(column: Int): ByteArray = cursor.getBlob(column)!!
    override fun getString(column: Int): String  = cursor.getString(column)!!
    override fun getLong(column: Int): Long      = cursor.getLong(column)
    override fun getInt(column: Int): Int        = cursor.getInt(column)
    override fun getShort(column: Int): Short    = cursor.getShort(column)
    override fun getFloat(column: Int): Float    = cursor.getFloat(column)
    override fun getDouble(column: Int): Double  = cursor.getDouble(column)
}