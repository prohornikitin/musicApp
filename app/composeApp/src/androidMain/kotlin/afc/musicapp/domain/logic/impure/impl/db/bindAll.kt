package afc.musicapp.domain.logic.impure.impl.db

import afc.musicapp.domain.logic.pure.sql.query.Arg
import afc.musicapp.domain.logic.pure.sql.query.BlobArg
import afc.musicapp.domain.logic.pure.sql.query.LongArg
import afc.musicapp.domain.logic.pure.sql.query.StringArg
import io.requery.android.database.sqlite.SQLiteProgram

fun SQLiteProgram.bindAll(args: List<Arg>) {
    args.forEachIndexed { i, arg ->
        val argI = i+1
        when(arg) {
            is BlobArg -> bindNullOr(argI, arg.data, ::bindBlob)
            is StringArg -> bindNullOr(argI, arg.data, ::bindString)
            is LongArg -> bindNullOr(argI, arg.data, ::bindLong)
        }
    }
}

fun <T: Any> SQLiteProgram.bindNullOr(index: Int, arg: T?, or: (Int, T)->Unit) {
    if(arg == null) {
        bindNull(index)
    } else {
        or(index, arg)
    }
}