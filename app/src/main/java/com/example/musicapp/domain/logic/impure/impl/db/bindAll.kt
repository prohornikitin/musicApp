package com.example.musicapp.domain.logic.impure.impl.db

import android.database.sqlite.SQLiteProgram
import com.example.musicapp.domain.logic.pure.query.Arg
import com.example.musicapp.domain.logic.pure.query.BlobArg
import com.example.musicapp.domain.logic.pure.query.LongArg
import com.example.musicapp.domain.logic.pure.query.StringArg

fun SQLiteProgram.bindAll(args: List<Arg>) {
    args.forEachIndexed { i, arg ->
        when(arg) {
            is BlobArg -> bindBlob(i, arg.data)
            is StringArg -> bindString(i, arg.data)
            is LongArg -> if (arg.data != null) {
                bindLong(i, arg.data!!)
            } else {
                bindNull(i)
            }
        }
    }
}