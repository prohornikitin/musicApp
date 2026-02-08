package afc.musicapp.ui.reusable.songcard

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.decodeToImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import afc.musicapp.domain.entities.SongCardStyle
import afc.musicapp.domain.entities.SongCardData

@Composable
fun SongCard(
    data: SongCardData,
    modifier: Modifier = Modifier,
    style: SongCardStyle = SongCardStyle(),
) {
    Row(modifier = modifier.padding(8.dp).wrapContentHeight()) {
        SongIcon(
            icon = data.icon?.decodeToImageBitmap(),
            visibility = style.iconVisibility,
            size = Dp(style.iconSizeDp.toFloat()),
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            StyledText(
                text = data.text.main,
                fontStyle = style.mainFont,
                modifier = Modifier.wrapContentSize(),
                maxLines = 1,
            )
            if (data.text.sub.isNotEmpty()) {
                StyledText(
                    text = data.text.sub,
                    fontStyle = style.bottomFont,
                    modifier = Modifier.wrapContentSize(),
                )
            }
        }
    }
}