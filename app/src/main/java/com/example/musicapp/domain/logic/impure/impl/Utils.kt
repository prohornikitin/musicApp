package com.example.musicapp.domain.logic.impure.impl

import android.content.Context
import android.content.ContextWrapper
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongCardData
import com.example.musicapp.domain.data.SongId

fun extractMediaMetadata(songCard: SongCardData, meta: Map<MetaKey, String>) = MediaMetadata.Builder()
    .setTitle(meta[MetaKey.Companion.TITLE])
    .setArtist(meta[MetaKey.Companion.ARTIST])
    .setAlbumTitle(meta[MetaKey.Companion.ALBUM])
    .setAlbumArtist(meta[MetaKey.Companion.ALBUM_ARTIST])
    .setArtworkData(songCard.iconBitmap, null)
    .setExtras(Bundle().apply {
        putString("mainText", songCard.mainText)
        putString("bottomText", songCard.bottomText)
    })
    .build()

val MediaItem.songId: SongId?
    get() = mediaId.toLongOrNull()?.let(::SongId)


fun f(context: Context) {
    context.filesDir
}