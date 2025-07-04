package com.example.musicapp.domain.di;

import com.example.musicapp.domain.logic.impure.iface.logger.LogConfig
import com.example.musicapp.domain.logic.impure.iface.logger.LogWriter
import com.example.musicapp.domain.logic.impure.impl.logger.Logger
import dagger.Binds;
import dagger.Module;
import dagger.Provides

@Module(includes = [DomainModule.BindsModule::class])
class DomainModule {
    @Provides
    fun logger(writer: LogWriter, config: LogConfig) =
        Logger(writer, config, "DEFAULT")

    @Module
    interface BindsModule {
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
