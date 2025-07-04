package com.example.musicapp.ui.reusable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.res.stringResource
import com.example.musicapp.R


@Composable
fun MenuOpenButton(onClick: () -> Unit) {
    IconButton(onClick) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = stringResource(R.string.open_menu),
        )
    }
}