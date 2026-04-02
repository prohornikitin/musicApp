package afc.musicapp.data.impure.impl.db

import afc.musicapp.data.pure.sql.mainDb.MainDbSetup
import android.annotation.SuppressLint
import afc.musicapp.domain.logic.impure.iface.Dispatchers
import afc.musicapp.domain.logic.impure.impl.logger.Logger
import android.content.Context

class MainDb private constructor(
    context: Context,
    logger: Logger,
    dispatchers: Dispatchers,
) : SqliteDb(context, "songs.db", MainDbSetup, logger, dispatchers) {
    companion object {
        @SuppressLint("StaticFieldLeak")
        private var instance: MainDb? = null

        fun getInstance(context: Context, logger: Logger, dispatchers: Dispatchers) = synchronized(this) {
            if (instance == null) {
                instance = MainDb(context.applicationContext, logger, dispatchers)
            }
            return@synchronized instance!!
        }
    }
}