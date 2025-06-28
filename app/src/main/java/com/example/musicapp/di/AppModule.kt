package com.example.musicapp.domain.logic.di

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.example.musicapp.PlayerFactoryImpl
import com.example.musicapp.domain.di.DomainModule
import com.example.musicapp.domain.logic.impure.iface.storage.read.GeneratedTemplatesStorage
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.GeneratedTemplatesEdit
import com.example.musicapp.domain.logic.impure.iface.MetaParser
import com.example.musicapp.domain.logic.impure.iface.PlayerFactory
import com.example.musicapp.domain.logic.impure.iface.storage.read.SongFileStorage
import com.example.musicapp.domain.logic.impure.iface.SongSearch
import com.example.musicapp.domain.logic.impure.iface.storage.read.Config
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.SongFileDbRemove
import com.example.musicapp.domain.logic.impure.iface.storage.read.SongThumbnailStorage
import com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed.SongThumbnailUpdate
import com.example.musicapp.domain.logic.impure.iface.storage.read.TemplatesConfig
import com.example.musicapp.domain.logic.impure.iface.storage.read.MetaKeyMapping
import com.example.musicapp.domain.logic.impure.iface.storage.read.MetaStorage
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.ConfigDbEdit
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.MetaDbRemove
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.MetaDbUpdate
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.SongFileDbAdd
import com.example.musicapp.domain.logic.impure.impl.MetaParserImpl
import com.example.musicapp.domain.logic.impure.impl.db.ConfigDb
import com.example.musicapp.domain.logic.impure.impl.db.MetaKeyDb
import com.example.musicapp.domain.logic.impure.impl.db.SongDb
import dagger.Binds
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent


@Module(includes = [AppModule.BindsModule::class, DomainModule::class])
@InstallIn(SingletonComponent::class)
class AppModule {
    @Provides
    fun songsDb(@ApplicationContext context: Context, templateStorage: TemplatesConfig): SongDb {
        return SongDb.getInstance(context)
    }

    @Provides
    fun metaKeyDb(@ApplicationContext context: Context): MetaKeyDb {
        return MetaKeyDb.getInstance(context)
    }

    @Provides
    fun kvConfigDb(@ApplicationContext context: Context): ConfigDb {
        return ConfigDb.getInstance(context)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {
        @Binds
        fun metaParser(it: MetaParserImpl): MetaParser

        @Binds
        fun config(it: ConfigDb): Config

        @Binds
        fun configDbEdit(it: ConfigDb): ConfigDbEdit

        @Binds
        fun metaDbRemove(it: SongDb): MetaDbRemove

        @Binds
        fun songFileDbAdd(it: SongDb): SongFileDbAdd

        @Binds
        fun songFileDbRemove(it: SongDb): SongFileDbRemove

        @Binds
        fun songSearch(it: SongDb): SongSearch

        @Binds
        fun songFilesStorage(it: SongDb): SongFileStorage

        @Binds
        fun metaStorage(it: SongDb): MetaStorage

        @Binds
        fun metaStorageEditor(it: SongDb): MetaDbUpdate

        @Binds
        fun generatedTemplatesStorage(it: SongDb): GeneratedTemplatesStorage

        @Binds
        fun player(it: MediaController): Player

        @Binds
        fun templatesStorageEditor(it: SongDb): GeneratedTemplatesEdit

        @Binds
        fun songThumbnailStorageEditor(it: SongDb): SongThumbnailUpdate

        @Binds
        fun songThumbnailStorage(it: SongDb): SongThumbnailStorage

        @Binds
        fun metaKeyMapping(it: MetaKeyDb): MetaKeyMapping

        @Binds
        fun playerFactory(it: PlayerFactoryImpl): PlayerFactory
    }
}