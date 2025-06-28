package com.example.musicapp.ui.reusable

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Snackbar
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import com.example.musicapp.R

@Composable
fun SearchBar(
    text: String,
    onTextChanged: (String) -> Unit,
    modifier: Modifier = Modifier,
    leadingIcon: @Composable () -> Unit = {},
) {
    val focusManager = LocalFocusManager.current
    var focus by remember { mutableStateOf(false) }
    TextField(
        value = text,
        onValueChange = onTextChanged,
        placeholder = {
            Text(stringResource(R.string.search))
        },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            imeAction = Done
        ),
        keyboardActions = KeyboardActions { focusManager.clearFocus() },
        modifier = modifier
            .background(Color.White)
            .onFocusChanged {
                focus = it.isFocused
            },
        leadingIcon = leadingIcon,
        trailingIcon = {
            if(focus) {
                IconButton(
                    onClick = {
                        onTextChanged("")
                        focusManager.clearFocus()
                    }
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = stringResource(R.string.close),
                    )
                }
            } else {
                Icon(
                    imageVector = Icons.Default.Search,
                    contentDescription = null,
                )
            }
        }
    )
}