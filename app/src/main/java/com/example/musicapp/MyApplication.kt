package com.example.musicapp

import android.app.Application
import android.content.Intent
import com.example.musicapp.domain.logic.impure.iface.MemoryCache
import dagger.hilt.android.HiltAndroidApp


@HiltAndroidApp
class MyApplication(
    private val caches: List<MemoryCache> = emptyList()
) : Application() {
    val playerServiceIntent by lazy {
        Intent(this, PlayerService::class.java)
    }

    override fun onCreate() {
        super.onCreate()
        startService(playerServiceIntent)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if(level >= TRIM_MEMORY_UI_HIDDEN) {
            caches.forEach {
                it.clearTheCache()
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopService(playerServiceIntent)
    }
}