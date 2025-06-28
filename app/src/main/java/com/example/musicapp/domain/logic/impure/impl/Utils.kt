package com.example.musicapp.domain.logic.impure.impl

import androidx.media3.common.MediaMetadata
import com.example.musicapp.domain.data.MetaKey

fun Map<MetaKey, String>.toMediaMetadata() = MediaMetadata.Builder()
    .setTitle(this[MetaKey.Companion.TITLE])
    .setArtist(this[MetaKey.Companion.ARTIST])
    .setAlbumTitle(this[MetaKey.Companion.ALBUM])
    .setAlbumArtist(this[MetaKey.Companion.ALBUM_ARTIST])
    .build()