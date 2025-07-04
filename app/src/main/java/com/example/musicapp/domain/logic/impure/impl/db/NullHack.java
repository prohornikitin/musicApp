package com.example.musicapp.domain.logic.impure.impl.db;

import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import androidx.annotation.NonNull;

import javax.annotation.Nullable;

/** @noinspection DataFlowIssue*/
class NullHack {
    static Cursor callSelect(
            SQLiteDatabase db,
            @Nullable SQLiteDatabase.CursorFactory cursorFactory,
            @NonNull String sql) {
        return db.rawQueryWithFactory(cursorFactory, sql, null, null);
    }

}
