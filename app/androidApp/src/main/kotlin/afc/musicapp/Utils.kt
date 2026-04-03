package afc.musicapp

import android.content.ContentResolver
import android.content.Context
import androidx.annotation.AnyRes
import okio.Path
import okio.Path.Companion.toPath


fun getDrawablePath(context: Context, @AnyRes drawableId: Int): Path {
    return context.resources.run {
        buildString {
            append(ContentResolver.SCHEME_ANDROID_RESOURCE)
            append("://")
            append(getResourcePackageName(drawableId))
            append('/')
            append(getResourceTypeName(drawableId))
            append('/')
            append(getResourceEntryName(drawableId))
        }.toPath()
    }
}