package afc.musicapp.domain.entities.codec

import afc.musicapp.domain.entities.FontFamily
import kotlin.enums.enumEntries

object FontFamilySerializer : EnumSerializer<FontFamily>(enumEntries<FontFamily>(), FontFamily.Default)