package afc.musicapp.app_common.ui.reusable.songcard

import afc.musicapp.app_common.ui.toComposeColor
import afc.musicapp.app_common.ui.toComposeFontFamily
import afc.musicapp.app_common.ui.toComposeFontStyle
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import afc.musicapp.domain.entities.SongCardStyle

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