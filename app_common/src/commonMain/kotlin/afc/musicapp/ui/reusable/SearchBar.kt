package afc.musicapp.ui.reusable

import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction.Companion.Done
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.close
import musicapp.app_common.generated.resources.search
import org.jetbrains.compose.resources.stringResource

@OptIn(ExperimentalMaterial3Api::class)
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
            Text(stringResource(Res.string.search))
        },
        maxLines = 1,
        keyboardOptions = KeyboardOptions(
            imeAction = Done
        ),
        keyboardActions = KeyboardActions { focusManager.clearFocus() },
        modifier = modifier
            .onFocusChanged {
                focus = it.isFocused
            },
        colors = TextFieldDefaults.colors(
            focusedContainerColor = MaterialTheme.colorScheme.background,
            unfocusedContainerColor = MaterialTheme.colorScheme.background,
        ),
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
                        contentDescription = stringResource(Res.string.close),
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