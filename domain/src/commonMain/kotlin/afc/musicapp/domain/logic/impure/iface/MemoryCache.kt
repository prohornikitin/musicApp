package afc.musicapp.domain.logic.impure.iface

import kotlinx.coroutines.runBlocking
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock

fun interface MemoryCache {
    fun clearTheCache()

    companion object {
        private val mCaches: MutableList<MemoryCache> = mutableListOf()
        private val mutex = Mutex()

        fun add(c: MemoryCache) = runBlocking {
            mutex.withLock {
                mCaches.add(c)
            }
        }

        suspend fun cleanupAll() = mutex.withLock {
            mCaches.forEach {
                it.clearTheCache()
            }
        }
    }
}