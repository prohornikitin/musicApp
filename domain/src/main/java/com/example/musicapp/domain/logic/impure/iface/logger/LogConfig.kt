package com.example.musicapp.domain.logic.impure.iface.logger

import com.example.musicapp.domain.logic.pure.logger.LogLevel

interface LogConfig {
    val currentLevel: LogLevel
}