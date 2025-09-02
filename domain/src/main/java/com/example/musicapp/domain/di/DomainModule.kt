package com.example.musicapp.domain.di

import com.example.musicapp.domain.logic.impure.iface.SongFileImport
import com.example.musicapp.domain.logic.impure.iface.SongSearch
import com.example.musicapp.domain.logic.impure.iface.logger.LogConfig
import com.example.musicapp.domain.logic.impure.iface.logger.LogWriter
import com.example.musicapp.domain.logic.impure.iface.storage.v2.read.MetaKeyMappingStorage
import com.example.musicapp.domain.logic.impure.iface.storage.v2.read.SongCardDataStorage
import com.example.musicapp.domain.logic.impure.impl.MetaKeyMappingStorageImpl
import com.example.musicapp.domain.logic.impure.impl.SongCardDataStorageImpl
import com.example.musicapp.domain.logic.impure.impl.SongFileImportImpl
import com.example.musicapp.domain.logic.impure.impl.SongSearchImpl
import com.example.musicapp.domain.logic.impure.impl.logger.Logger
import dagger.Binds
import dagger.Module
import dagger.Provides

@Module(includes = [DomainModule.BindsModule::class])
class DomainModule {
    @Provides
    fun logger(writer: LogWriter, config: LogConfig) =
        Logger(writer, config, "DEFAULT")

    @Module
    interface BindsModule {
        @Binds
        fun songSearch(it: SongSearchImpl): SongSearch

        @Binds
        fun songCardDataStorage(it: SongCardDataStorageImpl): SongCardDataStorage

        @Binds
        fun songFileImport(it: SongFileImportImpl): SongFileImport

        @Binds
        fun metaKeyMappingStorage(it: MetaKeyMappingStorageImpl): MetaKeyMappingStorage
//        @Binds
//        fun templateStorageEditor(it: TemplatesUpdateImpl): TemplateUpdate
//
//        @Binds
//        fun metaKeyMapping(it: TemplatesConfigDbEditImpl): TemplatesConfigDbEdit
//
//        @Binds
//        fun templatesConfig(it: TemplatesConfigImpl): TemplatesConfig
//
//        @Binds
//        fun songRemove(it: SongRemoveImpl): SongRemove
//
//        @Binds
//        fun songAdd(it: SongAddImpl): SongAdd
    }
}
