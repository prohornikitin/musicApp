package afc.musicapp.data.impure.impl.db

import afc.musicapp.data.impure.iface.db.query.Arg
import android.database.Cursor
import io.requery.android.database.sqlite.SQLiteCursor
import io.requery.android.database.sqlite.SQLiteCursorDriver
import io.requery.android.database.sqlite.SQLiteDatabase
import io.requery.android.database.sqlite.SQLiteQuery

class CustomSelectCursorFactory(val boundArgs: List<Arg>) : SQLiteDatabase.CursorFactory {
    override fun newCursor(
        db: SQLiteDatabase?,
        masterQuery: SQLiteCursorDriver?,
        editTable: String?,
        query: SQLiteQuery?
    ): Cursor? {
        if(query == null) {
            return null
        }
        query.bindAll(boundArgs)
        return SQLiteCursor(masterQuery, editTable, query)
    }
}