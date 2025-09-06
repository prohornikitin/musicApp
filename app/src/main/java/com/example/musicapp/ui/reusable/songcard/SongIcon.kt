package com.example.musicapp.ui.reusable.songcard

import android.graphics.Bitmap
import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.asImageBitmap
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.example.musicapp.domain.data.SongCardStyle.IconVisibility
import com.example.musicapp.domain.data.SongCardStyle.IconVisibility.*

@Preview
@Composable
fun SongIcon(
    modifier: Modifier = Modifier,
    icon: Bitmap? = null,
    visibility: IconVisibility = Show,
    size: Dp = 20.dp,
){
    Box(modifier) {
        if (icon != null) {
            when (visibility) {
                Show,
                ShowIfAvailable -> Image(
                    icon.asImageBitmap(),
                    null,
                    modifier = Modifier.size(size).clip(RoundedCornerShape(8.dp)),
                )
                Hide -> {}
            }
        } else {
            when (visibility) {
                Show -> Surface(
                        modifier = Modifier.size(size),
                        color = Color.LightGray,
                        shape = RoundedCornerShape(8.dp),
                    ) {}
                ShowIfAvailable,
                Hide-> {}
            }
        }
    }
}