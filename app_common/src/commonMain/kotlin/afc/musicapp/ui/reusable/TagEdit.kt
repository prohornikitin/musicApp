package afc.musicapp.ui.reusable

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.defaultMinSize
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import afc.musicapp.domain.entities.MetaKey
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.delete
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TagEditItem(
    key: MetaKey,
    value: String,
    onValueChange: (String) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier = modifier
        .padding(6.dp)
        .clip(RoundedCornerShape(8.dp,8.dp,8.dp,8.dp))
        .background(Color.LightGray)
    ) {
        IconButton(
            modifier = Modifier.align(Alignment.CenterVertically).defaultMinSize(20.dp),
            onClick = { onDeleteClick() },
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(Res.string.delete),
            )
        }
        Text(
            text = key.raw,
            maxLines = 1,
            fontSize = 24.sp,
            modifier = Modifier
                .fillMaxHeight()
                .align(Alignment.CenterVertically)
                .padding(horizontal = 6.dp)
        )
        Spacer(modifier = Modifier.weight(1f))
        BasicTextField(
            modifier = Modifier
                .wrapContentHeight()
                .align(Alignment.CenterVertically)
                .border(1.dp, Color.DarkGray, RoundedCornerShape(8.dp,8.dp,8.dp,8.dp))
                .padding(7.dp),
            textStyle = TextStyle(fontSize = 24.sp),
            value = value,
            onValueChange = onValueChange,
        )
    }
}