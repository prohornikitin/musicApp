package com.example.musicapp.di

import android.content.Context
import com.example.musicapp.PlayerImpl
import com.example.musicapp.domain.di.DomainModule
import com.example.musicapp.domain.logic.impure.iface.FormattedMetaRead
import com.example.musicapp.domain.logic.impure.iface.FileMetaParser
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import com.example.musicapp.domain.logic.impure.iface.logger.LogConfig
import com.example.musicapp.domain.logic.impure.iface.logger.LogWriter
import com.example.musicapp.domain.logic.impure.iface.player.Player
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.SongCardTextStorage
import com.example.musicapp.domain.logic.impure.iface.storage.l1.read.SongFiles
import com.example.musicapp.domain.logic.impure.impl.LogCatWriter
import com.example.musicapp.domain.logic.impure.impl.MetaFileParserImpl
import com.example.musicapp.domain.logic.impure.impl.db.MainDb
import com.example.musicapp.domain.logic.impure.impl.logger.Logger
import com.example.musicapp.domain.logic.pure.logger.LogLevel
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
        metaStorage: FormattedMetaRead,
        songCardTextStorage: SongCardTextStorage,
        fileStorage: SongFiles,
    ): Player = PlayerImpl(
        suspend { createMedia3Player(context) },
        metaStorage,
        songCardTextStorage,
        fileStorage,
    )

    @Provides
    fun mainDb(@ApplicationContext context: Context, logger: Logger): MainDb {
        return MainDb.getInstance(context, logger)
    }

    @Provides
    fun logWriter(): LogWriter = LogCatWriter()

    @Provides
    fun logConfig(): LogConfig = object : LogConfig {
        override val currentLevel: LogLevel
            get() = LogLevel.DEBUG

    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {
        @Binds
        fun metaParser(it: MetaFileParserImpl): FileMetaParser

        @Binds
        fun configDbEdit(it: MainDb): DbQueryInterpreter
    }
}