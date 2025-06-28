package com.example.config.serializer

import com.example.config.FontFamily
import kotlin.enums.enumEntries

object FontFamilySerializer : EnumSerializer<FontFamily>(enumEntries<FontFamily>(), FontFamily.Default)