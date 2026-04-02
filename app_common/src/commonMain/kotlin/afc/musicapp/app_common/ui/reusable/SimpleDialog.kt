package afc.musicapp.app_common.ui.reusable

import afc.musicapp.app_common.uistate.state.SimpleDialogConfig
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.cancel
import org.jetbrains.compose.resources.stringResource

@Composable
fun SimpleDialog(config: SimpleDialogConfig, dismiss: () -> Unit) {
    AlertDialog(
        onDismissRequest = {},
        confirmButton = {
            Button(
                onClick = { config.onClick() },
                content = { Text(stringResource(config.confirmButtonText)) },
            )
        },
        dismissButton = {
            Button(
                onClick = { dismiss() },
                content = { Text(stringResource(Res.string.cancel)) }
            )
        },
        title = {
            Text(stringResource(config.title))
        },
        text = {
            Text(config.text())
        },
    )
}