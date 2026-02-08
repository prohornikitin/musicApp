package afc.musicapp.domain.logic.impure.impl

import android.util.Log
import afc.musicapp.domain.logic.impure.iface.logger.LogWriter
import afc.musicapp.domain.logic.pure.logger.LogLevel

class LogCatWriter : LogWriter {
    companion object {
        const val LOGCAT_MAX_LINE_LENGTH = 4000
    }
    override fun write(level: LogLevel, tag: String, content: String) {
        val logTagLevel = when(level) {
            LogLevel.DEBUG -> Log.DEBUG
            LogLevel.INFO -> Log.INFO
            LogLevel.WARNING -> Log.WARN
            LogLevel.ERROR -> Log.ERROR
        }
        content.chunked(LOGCAT_MAX_LINE_LENGTH).forEach {
            Log.println(logTagLevel, tag, it)
        }
    }
}