package afc.musicapp

import android.app.Application
import android.content.Context
import android.content.Intent
import afc.musicapp.di.platformModule
import afc.musicapp.domain.logic.impure.iface.MemoryCache
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.launch
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bindProvider


class MyApplication() : Application(), DIAware {
    override val di = DI.lazy {
        bindProvider<Context> {
            this@MyApplication.applicationContext
        }
        import(platformModule)
    }

    private val playerServiceIntent by lazy {
        Intent(this, PlayerService::class.java)
    }

    private val scope: CoroutineScope = CoroutineScope(Dispatchers.Default)

    override fun onCreate() {
        super.onCreate()
        startService(playerServiceIntent)
    }

    override fun onTrimMemory(level: Int) {
        super.onTrimMemory(level)
        if(level >= TRIM_MEMORY_UI_HIDDEN) {
            scope.launch {
                MemoryCache.cleanupAll()
            }
        }
    }

    override fun onTerminate() {
        super.onTerminate()
        stopService(playerServiceIntent)
        scope.cancel("")
    }
}