package com.example.musicapp.domain.data.serializer

import com.example.musicapp.domain.data.FontStyle
import kotlin.enums.enumEntries

object FontStyleSerializer : EnumSerializer<FontStyle>(enumEntries<FontStyle>(), FontStyle.Normal)