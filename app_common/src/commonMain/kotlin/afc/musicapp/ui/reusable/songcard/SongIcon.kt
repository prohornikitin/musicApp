package afc.musicapp.ui.reusable.songcard

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ImageBitmap
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import afc.musicapp.domain.entities.SongCardStyle.IconVisibility
import afc.musicapp.domain.entities.SongCardStyle.IconVisibility.*

@Composable
fun SongIcon(
    modifier: Modifier = Modifier,
    icon: ImageBitmap? = null,
    visibility: IconVisibility = Show,
    size: Dp = 20.dp,
){
    Box(modifier) {
        if (icon != null) {
            when (visibility) {
                Show,
                ShowIfAvailable -> Image(
                    icon,
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