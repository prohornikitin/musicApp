package afc.musicapp.domain.logic.impure.impl.logger

import afc.musicapp.domain.logic.impure.iface.logger.LogConfig
import afc.musicapp.domain.logic.impure.iface.logger.LogWriter
import afc.musicapp.domain.logic.pure.logger.LogLevel

class Logger(
    private val writer: LogWriter,
    private val config: LogConfig,
    private val tag: String = "NO_TAG",
) {
    fun withTag(newTag: String) = Logger(writer, config, newTag)

    fun debug(lazyMsg: () -> String) = writeOrSkip(LogLevel.DEBUG, tag, lazyMsg)
    fun debug(msg: String) = writeOrSkip(LogLevel.DEBUG, tag, msg)
    fun info(msg: String) = writeOrSkip(LogLevel.INFO, tag, msg)
    fun warn(msg: String) = writeOrSkip(LogLevel.WARNING, tag, msg)
    fun error(msg: String) = writeOrSkip(LogLevel.ERROR, tag, msg)
    fun error(throwable: Throwable) = writeOrSkip(LogLevel.ERROR, tag) { throwable.stackTraceToString() }

    private fun writeOrSkip(level: LogLevel, tag: String, msg: String) {
        if (level.priority >= config.currentLevel.priority) {
            writer.write(level, tag, msg)
        }
    }

    private fun writeOrSkip(level: LogLevel, tag: String, lazyMsg: () -> String) {
        if (level.priority >= config.currentLevel.priority) {
            writer.write(level, tag, lazyMsg())
        }
    }

}