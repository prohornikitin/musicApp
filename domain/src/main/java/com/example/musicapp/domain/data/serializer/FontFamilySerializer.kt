package com.example.musicapp.domain.data.serializer

import com.example.musicapp.domain.data.FontFamily
import kotlin.enums.enumEntries

object FontFamilySerializer : EnumSerializer<FontFamily>(enumEntries<FontFamily>(), FontFamily.Default)