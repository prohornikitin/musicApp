package afc.musicapp.domain.entities.codec

import afc.musicapp.domain.entities.SongCardStyle
import kotlin.enums.enumEntries

object IconVisibilitySerializer : EnumSerializer<SongCardStyle.IconVisibility>(enumEntries<SongCardStyle.IconVisibility>(), SongCardStyle.IconVisibility.Show)