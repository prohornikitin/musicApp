package afc.musicapp.data.di

import org.kodein.di.DI

actual val platformModule = DI.Module("data-jvm") {
    TODO()
//    bindSingletonOf(::MetaFileParserImpl)
//    bindSingleton<FileMetaParser> { instance<MetaFileParserImpl>() }
//    bindSingleton<FileMetaUpdate> { instance<MetaFileParserImpl>() }
}