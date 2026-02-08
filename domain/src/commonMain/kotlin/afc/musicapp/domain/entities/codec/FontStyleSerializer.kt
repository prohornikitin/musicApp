package afc.musicapp.domain.entities.codec

import afc.musicapp.domain.entities.FontStyle
import kotlin.enums.enumEntries

object FontStyleSerializer : EnumSerializer<FontStyle>(enumEntries<FontStyle>(), FontStyle.Normal)