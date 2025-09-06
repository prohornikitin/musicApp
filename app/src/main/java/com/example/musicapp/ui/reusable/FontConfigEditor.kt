package com.example.musicapp.ui.reusable

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.LocaleList
import com.example.musicapp.domain.data.FontFamily
import com.example.musicapp.domain.data.FontStyle
import com.example.musicapp.domain.data.SongCardStyle.FontConfig
import com.example.musicapp.R


@Composable
fun FontConfigEditor(
    value: FontConfig,
    onValueChange: (FontConfig) -> Unit,
    modifier: Modifier = Modifier,
) {
    Column(modifier) {
        Spinner(
            "Font family",
            options = FontFamily.entries,
            selected = value.family,
            onSelectionChanged = { onValueChange(value.copy(family = it)) },
            getName = { it.toString() },
        )

        Spinner(
            stringResource(R.string.font_style),
            options = FontStyle.entries,
            selected = value.style,
            onSelectionChanged = { onValueChange(value.copy(style = it)) },
            getName = { it.toString().capitalize(LocaleList.current) },
        )

        IntSlider(
            value = value.sizeSp,
            onValueChange = { onValueChange(value.copy(sizeSp = it)) },
            valueRange = 6..30,
            label = stringResource(R.string.font_size),
        )

        IntSlider(
            value = value.weight,
            onValueChange = { onValueChange(value.copy(weight = it)) },
            valueRange = 100..1000,
            steps = 8,
            label = stringResource(R.string.font_weight),
        )

        ColorPickerFromHex(
            label = stringResource(R.string.color),
            color = value.color,
            onColorPick = { onValueChange(value.copy(color = it)) }
        )
    }
}