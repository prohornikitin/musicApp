package afc.musicapp.domain.logic.impure.iface.logger

import afc.musicapp.domain.logic.pure.logger.LogLevel

interface LogConfig {
    val currentLevel: LogLevel
}