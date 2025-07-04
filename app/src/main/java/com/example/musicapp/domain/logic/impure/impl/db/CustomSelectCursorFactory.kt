package com.example.musicapp.domain.logic.impure.impl.db

import android.database.Cursor
import android.database.sqlite.SQLiteCursor
import android.database.sqlite.SQLiteCursorDriver
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteQuery
import com.example.musicapp.domain.logic.pure.query.Arg

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