package afc.musicapp.di

import afc.musicapp.domain.logic.impure.iface.Dispatchers
import afc.musicapp.domain.logic.impure.impl.logger.Logger
import kotlinx.coroutines.IO
import okio.FileSystem
import okio.SYSTEM
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.instance

val domainModule = DI.Module("domain") {
    bindSingleton {
        Logger(instance(), instance(), "DEFAULT")
    }

    bindSingleton<Dispatchers> {
        object : Dispatchers {
            override val io = kotlinx.coroutines.Dispatchers.IO
            override val default = kotlinx.coroutines.Dispatchers.Default
            override val main = kotlinx.coroutines.Dispatchers.Main
        }
    }

    bindSingleton { FileSystem.SYSTEM }
}