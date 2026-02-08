package afc.musicapp.domain.logic.impure.impl.db

import android.annotation.SuppressLint
import afc.musicapp.di.context.ApplicationContext
import afc.musicapp.domain.logic.impure.iface.Dispatchers
import afc.musicapp.domain.logic.impure.impl.logger.Logger
import afc.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup

class MainDb private constructor(
    context: ApplicationContext,
    logger: Logger,
    dispatchers: Dispatchers,
) : SqliteDb(context, "songs.db", MainDbSetup, logger, dispatchers) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: MainDb? = null

        fun getInstance(context: ApplicationContext, logger: Logger, dispatchers: Dispatchers) = synchronized(this) {
            if (instance == null) {
                instance = MainDb(context, logger, dispatchers)
            }
            return@synchronized instance!!
        }
    }

//    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
//        upgrade(oldVersion).forEach {
//            db?.execSQL(it.toString())
//        }
//    }
}