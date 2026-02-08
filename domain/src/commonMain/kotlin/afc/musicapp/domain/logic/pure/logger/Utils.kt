package afc.musicapp.domain.logic.pure.logger

import afc.musicapp.domain.logic.impure.impl.logger.Logger

inline fun <reified T> Logger.withClassTag() = withTag(T::class.simpleName ?: T::class.toString())

inline fun <reified T> Logger.withClassTag(obj: T) = withTag(T::class.simpleName ?: T::class.toString())
