package afc.musicapp.ui.reusable

import androidx.compose.foundation.layout.Column
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.capitalize
import androidx.compose.ui.text.intl.LocaleList
import afc.musicapp.domain.entities.FontFamily
import afc.musicapp.domain.entities.FontStyle
import afc.musicapp.domain.entities.SongCardStyle.FontConfig
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.color
import musicapp.app_common.generated.resources.font_size
import musicapp.app_common.generated.resources.font_style
import musicapp.app_common.generated.resources.font_weight
import org.jetbrains.compose.resources.stringResource


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
            stringResource(Res.string.font_style),
            options = FontStyle.entries,
            selected = value.style,
            onSelectionChanged = { onValueChange(value.copy(style = it)) },
            getName = { it.toString().capitalize(LocaleList.current) },
        )

        IntSlider(
            value = value.sizeSp,
            onValueChange = { onValueChange(value.copy(sizeSp = it)) },
            valueRange = 6..30,
            label = stringResource(Res.string.font_size),
        )

        IntSlider(
            value = value.weight,
            onValueChange = { onValueChange(value.copy(weight = it)) },
            valueRange = 100..1000,
            steps = 8,
            label = stringResource(Res.string.font_weight),
        )

        ColorPickerFromHex(
            label = stringResource(Res.string.color),
            color = value.color,
            onColorPick = { onValueChange(value.copy(color = it)) }
        )
    }
}