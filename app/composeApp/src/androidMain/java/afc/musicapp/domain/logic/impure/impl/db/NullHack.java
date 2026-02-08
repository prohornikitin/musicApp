package afc.musicapp.domain.logic.impure.impl.db;


import android.database.Cursor;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.requery.android.database.sqlite.SQLiteDatabase;


class NullHack {
    static Cursor callSelect(
            SQLiteDatabase db,
            @Nullable SQLiteDatabase.CursorFactory cursorFactory,
            @NonNull String sql) {
        return db.rawQueryWithFactory(cursorFactory, sql, null, null);
    }
}
