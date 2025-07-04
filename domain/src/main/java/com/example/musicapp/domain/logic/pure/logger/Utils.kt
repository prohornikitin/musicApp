package com.example.musicapp.domain.logic.pure.logger

import com.example.musicapp.domain.logic.impure.impl.logger.Logger

inline fun <reified T> Logger.withClassTag() = withTag(T::class.simpleName ?: T::class.java.name)

inline fun <reified T> Logger.withClassTag(obj: T) = withTag(T::class.simpleName ?: T::class.java.name)
