package afc.musicapp.di

import afc.musicapp.PlayerImpl
import afc.musicapp.app_common.di.appModule
import afc.musicapp.domain.logic.impure.iface.PlatformValues
import afc.musicapp.domain.logic.impure.iface.player.Player
import afc.musicapp.domain.logic.impure.impl.AndroidValues
import afc.musicapp.domain.logic.impure.impl.LogCatWriter
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.bindSingletonOf
import org.kodein.di.instance
import org.kodein.di.new

val platformModule = DI.Module("platform") {
    import(appModule)

    bindSingleton<Player> {
        PlayerImpl(
            suspend { createMedia3Player(instance()) },
            instance(),
            instance(),
            instance(),
            instance(),
            instance(),
        )
    }

    bindSingletonOf(::LogCatWriter)
    bindSingleton<PlatformValues> { new(::AndroidValues) }
}