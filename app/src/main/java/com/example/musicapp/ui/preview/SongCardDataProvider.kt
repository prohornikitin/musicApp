package com.example.musicapp.ui.preview

import androidx.compose.ui.tooling.preview.PreviewParameterProvider
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.data.SongCardData

class SongCardDataProvider : PreviewParameterProvider<SongCardData> {
    override val values = sequenceOf(
        SongCardData(SongId(0), "Main text", "Bottom text"),
//        SongCardData(SongId(0), "Main text", "Bottom text", icon = imageResource(R.drawable.btn_star)),
    )
}