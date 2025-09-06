package com.example.musicapp.domain.di

import com.example.musicapp.domain.logic.impure.CardTemplatesEdit
import com.example.musicapp.domain.logic.impure.iface.FormattedMetaRead
import com.example.musicapp.domain.logic.impure.iface.SongFileImport
import com.example.musicapp.domain.logic.impure.iface.SongSearch
import com.example.musicapp.domain.logic.impure.iface.logger.LogConfig
import com.example.musicapp.domain.logic.impure.iface.logger.LogWriter
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.KvConfigRead
import com.example.musicapp.domain.logic.impure.iface.storage.l2.read.MetaFormatConfigRead
import com.example.musicapp.domain.logic.impure.iface.storage.l2.read.TemplatesConfigRead
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.MetaKeyMappingStorage
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.MetaStorage
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.SongCardTextStorage
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.SongFiles
import com.example.musicapp.domain.logic.impure.iface.storage.l1.write.KvConfigUpdate
import com.example.musicapp.domain.logic.impure.impl.storage.l2.CardTemplatesEditImpl
import com.example.musicapp.domain.logic.impure.impl.storage.l2.TemplatesConfigImpl
import com.example.musicapp.domain.logic.impure.impl.FormattedMetaStorageImpl
import com.example.musicapp.domain.logic.impure.impl.storage.l1.MetaKeyMappingStorageImpl
import com.example.musicapp.domain.logic.impure.impl.storage.l1.MetaStorageImpl
import com.example.musicapp.domain.logic.impure.impl.storage.l1.SongCardTextStorageImpl
import com.example.musicapp.domain.logic.impure.impl.SongFileImportImpl
import com.example.musicapp.domain.logic.impure.impl.storage.l1.SongFilesImpl
import com.example.musicapp.domain.logic.impure.impl.storage.l1.SongSearchImpl
import com.example.musicapp.domain.logic.impure.impl.storage.l1.SongThumbnailStorageImpl
import com.example.musicapp.domain.logic.impure.impl.logger.Logger
import com.example.musicapp.domain.logic.impure.impl.storage.l1.KvConfigImpl
import com.example.musicapp.domain.logic.impure.impl.storage.l2.MetaFormatConfigImpl
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
        fun cardTemplatesEdit(it: CardTemplatesEditImpl): CardTemplatesEdit

        @Binds
        fun songSearch(it: SongSearchImpl): SongSearch

        @Binds
        fun songCardDataStorage(it: SongCardTextStorageImpl): SongCardTextStorage

        @Binds
        fun songFileImport(it: SongFileImportImpl): SongFileImport

        @Binds
        fun metaKeyMappingStorage(it: MetaKeyMappingStorageImpl): MetaKeyMappingStorage

        @Binds
        fun formattedMetaStorage(it: FormattedMetaStorageImpl): FormattedMetaRead

        @Binds
        fun songFiles(it: SongFilesImpl): SongFiles

        @Binds
        fun metaStorage(it: MetaStorageImpl): MetaStorage

        @Binds
        fun metaFormatConfig(it: MetaFormatConfigImpl): MetaFormatConfigRead

        @Binds
        fun templatesConfig(it: TemplatesConfigImpl): TemplatesConfigRead

        @Binds
        fun kvConfigRead(it: KvConfigImpl): KvConfigRead

        @Binds
        fun kvConfigUpdate(it: KvConfigImpl): KvConfigUpdate
    }
}
