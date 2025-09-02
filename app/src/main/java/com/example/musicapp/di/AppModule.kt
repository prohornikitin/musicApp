package com.example.musicapp.di

import android.content.Context
import com.example.musicapp.PlayerImpl
import com.example.musicapp.domain.di.DomainModule
import com.example.musicapp.domain.logic.impure.iface.FormattedMetaStorage
import com.example.musicapp.domain.logic.impure.iface.MetaParser
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.impure.iface.player.Player
import com.example.musicapp.domain.logic.impure.iface.storage.v2.read.SongCardDataStorage
import com.example.musicapp.domain.logic.impure.iface.storage.v2.read.SongFiles
import com.example.musicapp.domain.logic.impure.impl.MetaParserImpl
import com.example.musicapp.domain.logic.impure.impl.db.MainDb
import com.example.musicapp.domain.logic.impure.impl.logger.Logger
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
    fun player(
        @ApplicationContext context: Context,
        metaStorage: FormattedMetaStorage,
        songCardDataStorage: SongCardDataStorage,
        fileStorage: SongFiles,
    ): Player = PlayerImpl(
        suspend { createMedia3Player(context) },
        metaStorage,
        songCardDataStorage,
        fileStorage,
    )

    @Provides
    fun mainDb(@ApplicationContext context: Context, logger: Logger): MainDb {
        return MainDb.getInstance(context, logger)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {
        @Binds
        fun metaParser(it: MetaParserImpl): MetaParser

        @Binds
        fun configDbEdit(it: MainDb): DbQueryInterpreter
    }
}