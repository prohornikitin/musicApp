package com.example.musicapp.domain.data.serializer

import com.example.musicapp.domain.data.SongCardStyle
import kotlin.enums.enumEntries

object IconVisibilitySerializer : EnumSerializer<SongCardStyle.IconVisibility>(enumEntries<SongCardStyle.IconVisibility>(), SongCardStyle.IconVisibility.Show)