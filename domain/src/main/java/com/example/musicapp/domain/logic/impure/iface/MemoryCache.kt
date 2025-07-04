package com.example.musicapp.domain.logic.impure.iface

fun interface MemoryCache {
    fun clearTheCache()

    companion object {
        private val mCaches: MutableList<MemoryCache> = mutableListOf()

        fun add(c: MemoryCache) = synchronized(this) {
            mCaches.add(c)
        }

        fun cleanupAll() {
            mCaches.forEach {
                it.clearTheCache()
            }
        }
    }
}