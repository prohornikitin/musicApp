package afc.musicapp.ui.reusable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import afc.musicapp.domain.entities.RgbaColor
import afc.musicapp.domain.toColorOrNull
import afc.musicapp.domain.toHexStr
import afc.musicapp.ui.toComposeColor
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.error
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ColorPickerFromHex(
    color: RgbaColor,
    onColorPick: (RgbaColor) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Color",
) {
    var text by remember { mutableStateOf("#${color.raw.toHexStr()}") }
    var errorRes by remember { mutableStateOf<StringResource?>(null) }
    OutlinedTextField(
        value = text,
        modifier = modifier,
        isError = errorRes != null,
        supportingText = { errorRes?.let { Text(stringResource(it)) } },
        onValueChange = {
            text = it
            val newColor = it.toColorOrNull()
            if (newColor != null) {
                errorRes = null
                onColorPick(newColor)
            } else {
                errorRes = Res.string.error
            }
        },
        label = { Text(label) },
        trailingIcon = { Icon(Icons.Default.Star, null, tint = color.toComposeColor()) },
    )
}