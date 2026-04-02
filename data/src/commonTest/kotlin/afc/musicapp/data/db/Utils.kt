package afc.musicapp.data.db

import afc.musicapp.domain.logic.impure.iface.logger.LogConfig
import afc.musicapp.domain.logic.impure.impl.logger.Logger
import afc.musicapp.domain.logic.pure.logger.LogLevel
import kotlin.test.assertEquals

fun assertCharSeqEquals(expected: CharSequence, actual: CharSequence) {
    assertEquals(expected.toString(), actual.toString())
}

fun createMockLogger() = Logger(
    writer = { a: LogLevel, b: String, c: String -> },
    config = LogConfig(LogLevel.ERROR),
)