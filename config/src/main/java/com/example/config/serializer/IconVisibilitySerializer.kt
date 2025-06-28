package com.example.config.serializer

import com.example.config.SongCardStyle.IconVisibility
import kotlin.enums.enumEntries

object IconVisibilitySerializer : EnumSerializer<IconVisibility>(enumEntries<IconVisibility>(), IconVisibility.Show)