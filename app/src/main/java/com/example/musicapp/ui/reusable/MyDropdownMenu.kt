package com.example.musicapp.ui.reusable

import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier


@Composable
fun <T> MyDropdownMenu(
    modifier: Modifier = Modifier,
    expanded: Boolean = false,
    items: Iterable<T>,
    getTitle: @Composable (T) -> String,
    onClick: (T) -> Unit,
    onDismiss: () -> Unit,
) {
    DropdownMenu(
        expanded,
        onDismissRequest = onDismiss,
        modifier = modifier,
    ) {
        items.forEach {
            DropdownMenuItem(
                onClick = { onClick(it) },
                text = {
                    Text(getTitle(it))
                }
            )
        }
    }
}