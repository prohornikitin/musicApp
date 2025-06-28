package com.example.config.serializer

import com.example.config.FontStyle
import kotlin.enums.enumEntries

object FontStyleSerializer : EnumSerializer<FontStyle>(enumEntries<FontStyle>(), FontStyle.Normal)