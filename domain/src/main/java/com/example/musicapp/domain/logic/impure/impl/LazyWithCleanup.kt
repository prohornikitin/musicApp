package com.example.musicapp.domain.logic.impure.impl

import com.example.musicapp.domain.logic.impure.iface.MemoryCache
import kotlin.reflect.KProperty

class LazyWithCleanup<T: Any>(private val calc: () -> T) {
    init {
        MemoryCache.Companion.add {
            cached = null
        }
    }

    private var cached: T? = null

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T {
        return cached ?: calc().also {
            cached = it
        }
    }
}