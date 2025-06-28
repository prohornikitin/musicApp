package com.example.musicapp.ui.reusable

import androidx.compose.foundation.layout.Column
import androidx.compose.material3.Slider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import kotlin.math.roundToInt


@Composable
fun IntSlider(
    value: Int,
    onValueChange: (Int) -> Unit,
    valueRange: IntRange,
    modifier: Modifier = Modifier,
    label: String = "",
    steps: Int? = null
) {
    val steps = steps ?: (valueRange.last - valueRange.first - 1)
    assert((valueRange.last - valueRange.first) % (steps+1) == 0) {
        "invalid steps value"
    }

    var pos by remember { mutableIntStateOf(value) }
    Column(modifier = modifier) {
        Text(text = "$label: $pos")
        Slider(
            value = pos.toFloat(),
            steps = steps,
            onValueChange = {
                pos = it.roundToInt()
                onValueChange(it.roundToInt())
            },
            valueRange = valueRange.first.toFloat()..valueRange.last.toFloat(),
        )
    }
}