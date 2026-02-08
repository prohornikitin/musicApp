package afc.musicapp.domain.logic.impure.iface.logger

import afc.musicapp.domain.logic.pure.logger.LogLevel

interface LogWriter {
    fun write(level: LogLevel, tag: String, content: String)
}