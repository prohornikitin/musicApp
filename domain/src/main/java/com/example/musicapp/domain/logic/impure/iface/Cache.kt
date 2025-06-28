package com.example.musicapp.domain.logic.impure.iface

interface Cache {
    fun clearTheCache()

    companion object {
        private val mCaches: MutableList<Cache> = mutableListOf()

        fun add(c: Cache) = synchronized(this) {
            mCaches.add(c)
        }

        fun cleanupAll() {
            mCaches.forEach {
                it.clearTheCache()
            }
        }
    }
}