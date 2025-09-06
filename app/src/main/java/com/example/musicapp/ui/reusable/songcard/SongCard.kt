package com.example.musicapp.ui.reusable.songcard

import android.graphics.BitmapFactory
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
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.musicapp.domain.data.SongCardStyle
import com.example.musicapp.domain.data.SongCardData
import com.example.musicapp.ui.preview.SongCardDataProvider

@Preview
@Composable
fun SongCard(
    @PreviewParameter(SongCardDataProvider::class) data: SongCardData,
    modifier: Modifier = Modifier,
    style: SongCardStyle = SongCardStyle(),
) {
    Row(modifier = modifier.padding(8.dp).wrapContentHeight()) {
        SongIcon(
            icon = data.iconBitmap?.let {BitmapFactory.decodeByteArray(data.iconBitmap, 0, it.size) },
            visibility = style.iconVisibility,
            size = Dp(style.iconSizeDp.toFloat()),
            modifier = Modifier
                .align(Alignment.CenterVertically)
        )
        Spacer(Modifier.width(8.dp))
        Column(modifier = Modifier.weight(1f)) {
            StyledText(
                text = data.mainText,
                fontStyle = style.mainFont,
                modifier = Modifier.wrapContentSize(),
                maxLines = 1,
            )
            if (data.bottomText.isNotEmpty()) {
                StyledText(
                    text = data.bottomText,
                    fontStyle = style.bottomFont,
                    modifier = Modifier.wrapContentSize(),
                )
            }
        }
    }
}