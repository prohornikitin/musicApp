package com.example.musicapp.ui.reusable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import com.example.config.RgbaColor
import com.example.musicapp.domain.toColorOrNull
import com.example.musicapp.domain.toHexStr
import com.example.musicapp.R
import com.example.musicapp.ui.toComposeColor


@Composable
fun ColorPickerFromHex(
    color: RgbaColor,
    onColorPick: (RgbaColor) -> Unit,
    modifier: Modifier = Modifier,
    label: String = "Color",
) {
    var text by remember { mutableStateOf("#${color.raw.toHexStr()}") }
    var errorRes by remember { mutableStateOf<Int?>(null) }
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
                errorRes = R.string.error
            }
        },
        label = { Text(label) },
        trailingIcon = { Icon(Icons.Default.Star, null, tint = color.toComposeColor()) },
    )
}