package afc.musicapp.data.di

import afc.musicapp.data.MetaFileParserImpl
import afc.musicapp.data.impure.iface.db.DbQueryInterpreter
import afc.musicapp.data.impure.impl.db.MainDb
import afc.musicapp.domain.logic.impure.iface.FileMetaParser
import afc.musicapp.domain.logic.impure.iface.FileMetaUpdate
import org.kodein.di.DI
import org.kodein.di.bindSingleton
import org.kodein.di.bindSingletonOf
import org.kodein.di.instance

actual val platformModule = DI.Module("data-android") {
    bindSingletonOf(::MetaFileParserImpl)
    bindSingleton<FileMetaParser> { instance<MetaFileParserImpl>() }
    bindSingleton<FileMetaUpdate> { instance<MetaFileParserImpl>() }

    bindSingleton<DbQueryInterpreter> {
        MainDb.getInstance(instance(), instance(), instance())
    }
}