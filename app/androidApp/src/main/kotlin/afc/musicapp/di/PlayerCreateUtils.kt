package afc.musicapp.di

import android.content.ComponentName
import android.content.Context
import androidx.media3.common.Player
import androidx.media3.session.MediaController
import androidx.media3.session.SessionToken
import afc.musicapp.PlayerService
import com.google.common.util.concurrent.MoreExecutors
import kotlin.coroutines.suspendCoroutine

suspend fun createMedia3Player(context: Context): Player {
    val appContext = context.applicationContext
    return suspendCoroutine { cont ->
        val sessionToken = SessionToken(appContext, ComponentName(appContext, PlayerService::class.java))
        val controllerFuture = MediaController.Builder(context, sessionToken).buildAsync()
        controllerFuture.addListener({
            cont.resumeWith(Result.success(controllerFuture.get()))
        }, MoreExecutors.directExecutor())
    }
}