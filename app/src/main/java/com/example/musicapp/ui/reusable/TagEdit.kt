package com.example.musicapp.ui.reusable

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import com.example.musicapp.R
import com.example.musicapp.domain.data.MetaKey

@Composable
fun TagEditItem(
    key: MetaKey,
    value: String,
    onValueChange: (String) -> Unit,
    onDeleteClick: () -> Unit,
    modifier: Modifier = Modifier,
) {
    Row(modifier) {
        IconButton(
            modifier = Modifier.fillMaxHeight(),
            onClick = { onDeleteClick }
        ) {
            Icon(
                imageVector = Icons.Default.Clear,
                contentDescription = stringResource(R.string.delete),
            )
        }
        Text(
            text = key.raw,
            maxLines = 1,
        )
        OutlinedTextField(
            value = value,
            onValueChange = onValueChange,
        )
    }
}