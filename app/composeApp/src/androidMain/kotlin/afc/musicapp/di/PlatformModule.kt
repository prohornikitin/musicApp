package afc.musicapp.di

import android.content.Context
import afc.musicapp.PlayerImpl
import afc.musicapp.di.context.ApplicationContext
import afc.musicapp.domain.logic.impure.iface.FileMetaParser
import afc.musicapp.domain.logic.impure.iface.FileMetaUpdate
import afc.musicapp.domain.logic.impure.iface.PlatformValues
import afc.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import afc.musicapp.domain.logic.impure.iface.player.Player
import afc.musicapp.domain.logic.impure.impl.AndroidValues
import afc.musicapp.domain.logic.impure.impl.LogCatWriter
import afc.musicapp.domain.logic.impure.impl.MetaFileParserImpl
import afc.musicapp.domain.logic.impure.impl.db.MainDb
import org.kodein.di.DI
import org.kodein.di.bindProvider
import org.kodein.di.bindSingleton
import org.kodein.di.bindSingletonOf
import org.kodein.di.instance
import org.kodein.di.new

val platformModule = DI.Module("platform") {
    import(appModule)

    bindProvider<ApplicationContext> {
        ApplicationContext.wrap(instance<Context>().applicationContext)
    }

    bindSingleton<Player> {
        PlayerImpl(
            suspend { createMedia3Player(instance()) },
            instance(),
            instance(),
            instance(),
            instance(),
        )
    }

    bindSingleton<DbQueryInterpreter> {
        MainDb.getInstance(instance(), instance(), instance())
    }

    bindSingletonOf(::LogCatWriter)

    bindSingletonOf(::MetaFileParserImpl)
    bindSingleton<FileMetaParser> { instance<MetaFileParserImpl>() }
    bindSingleton<FileMetaUpdate> { instance<MetaFileParserImpl>() }
    bindSingleton<PlatformValues> { new(::AndroidValues) }
}