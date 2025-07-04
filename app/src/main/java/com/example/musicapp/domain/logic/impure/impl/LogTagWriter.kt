package com.example.musicapp.domain.logic.impure.impl

import android.util.Log
import com.example.musicapp.domain.logic.impure.iface.logger.LogWriter
import com.example.musicapp.domain.logic.pure.logger.LogLevel

class LogTagWriter : LogWriter {
    override fun write(level: LogLevel, tag: String, string: String) {
        val logTagLevel = when(level) {
            LogLevel.DEBUG -> Log.DEBUG
            LogLevel.INFO -> Log.INFO
            LogLevel.WARNING -> Log.WARN
            LogLevel.ERROR -> Log.ERROR
        }
        Log.println(logTagLevel, tag, string)
    }
}