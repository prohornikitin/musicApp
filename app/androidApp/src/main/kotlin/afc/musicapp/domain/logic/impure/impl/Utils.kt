package afc.musicapp.domain.logic.impure.impl

import android.content.Context
import android.os.Bundle
import androidx.media3.common.MediaItem
import androidx.media3.common.MediaMetadata
import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SongCardData
import afc.musicapp.domain.entities.SongId

fun extractMediaMetadataIfAny(songCard: SongCardData?, meta: Map<MetaKey, String>) = MediaMetadata.Builder()
    .setTitle(meta[MetaKey.TITLE])
    .setArtist(meta[MetaKey.ARTIST])
    .setAlbumTitle(meta[MetaKey.ALBUM])
    .setAlbumArtist(meta[MetaKey.ALBUM_ARTIST])
//    .setArtworkUri(songCard.icon?.toFile()?.let(Uri::fromFile))
    .setExtras(Bundle().apply {
        putString("mainText", songCard?.text?.main)
        putString("bottomText", songCard?.text?.sub)
    })
    .build()

val MediaItem.songId: SongId?
    get() = mediaId.toLongOrNull()?.let(::SongId)


fun f(context: Context) {
    context.filesDir
}