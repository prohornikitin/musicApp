package afc.musicapp.data.impure.impl.db

import afc.musicapp.data.impure.iface.db.query.Arg
import afc.musicapp.data.impure.iface.db.query.BlobArg
import afc.musicapp.data.impure.iface.db.query.LongArg
import afc.musicapp.data.impure.iface.db.query.StringArg
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