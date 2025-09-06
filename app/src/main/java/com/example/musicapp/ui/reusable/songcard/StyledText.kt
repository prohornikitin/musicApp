package com.example.musicapp.ui.reusable.songcard

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import com.example.musicapp.domain.data.SongCardStyle
import com.example.musicapp.ui.toComposeColor
import com.example.musicapp.ui.toComposeFontFamily
import com.example.musicapp.ui.toComposeFontStyle

@Composable
fun StyledText(
    text: String,
    fontStyle: SongCardStyle.FontConfig,
    modifier: Modifier = Modifier,
    maxLines: Int = Int.MAX_VALUE,
) {
    fontStyle.apply {
        Text(
            text,
            modifier = modifier,
            fontSize = sizeSp.sp,
            fontStyle = style.toComposeFontStyle(),
            fontFamily = family.toComposeFontFamily(),
            fontWeight = FontWeight(weight),
            color = color.toComposeColor(),
            maxLines = maxLines,
        )
    }
}