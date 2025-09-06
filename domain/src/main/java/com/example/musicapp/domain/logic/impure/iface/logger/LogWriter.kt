package com.example.musicapp.domain.logic.impure.iface.logger

import com.example.musicapp.domain.logic.pure.logger.LogLevel

interface LogWriter {
    fun write(level: LogLevel, tag: String, content: String)
}