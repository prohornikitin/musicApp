package com.example.musicapp.domain.logic.impure.impl.db

import android.database.sqlite.SQLiteProgram
import com.example.musicapp.domain.logic.pure.query.Arg
import com.example.musicapp.domain.logic.pure.query.BlobArg
import com.example.musicapp.domain.logic.pure.query.LongArg
import com.example.musicapp.domain.logic.pure.query.StringArg

fun SQLiteProgram.bindAll(args: List<Arg>) {
    args.forEachIndexed { i, arg ->
        val argI = i+1
        when(arg) {
            is BlobArg -> bindBlob(argI, arg.data)
            is StringArg -> bindString(argI, arg.data)
            is LongArg -> if (arg.data != null) {
                bindLong(argI, arg.data!!)
            } else {
                bindNull(argI)
            }
        }
    }
}