package com.example.musicapp

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import com.example.musicapp.domain.logic.impure.iface.PlayerFactory
import com.google.common.util.concurrent.MoreExecutors
import dagger.hilt.android.qualifiers.ApplicationContext
import javax.inject.Inject
import kotlin.coroutines.suspendCoroutine

class PlayerFactoryImpl @Inject constructor(
    @ApplicationContext private val context: Context
) : PlayerFactory {
    override suspend fun get(): Player = suspendCoroutine { cont ->
        val sessionToken = SessionToken(context, ComponentName(context, PlayerService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            cont.resumeWith(Result.success(controllerFuture.get()))
        }, MoreExecutors.directExecutor())
    }
}