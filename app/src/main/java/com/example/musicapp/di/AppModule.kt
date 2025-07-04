package com.example.musicapp.domain.logic.di

import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import com.example.musicapp.PlayerFactoryImpl
import com.example.musicapp.domain.di.DomainModule
import com.example.musicapp.domain.logic.impure.iface.MetaParser
import com.example.musicapp.domain.logic.impure.iface.PlayerFactory
import com.example.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
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
    fun mainDb(@ApplicationContext context: Context, logger: Logger): MainDb {
        return MainDb.getInstance(context, logger)
    }

    @Module
    @InstallIn(SingletonComponent::class)
    interface BindsModule {
        @Binds
        fun metaParser(it: MetaParserImpl): MetaParser

        @Binds
        fun playerFactory(it: PlayerFactoryImpl): PlayerFactory

        @Binds
        fun configDbEdit(it: MainDb): DbQueryInterpreter
    }
}