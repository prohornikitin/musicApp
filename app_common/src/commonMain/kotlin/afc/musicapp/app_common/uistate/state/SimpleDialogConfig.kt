package afc.musicapp.app_common.uistate.state

import androidx.compose.runtime.Composable
import org.jetbrains.compose.resources.StringResource

data class SimpleDialogConfig(
    val title: StringResource,
    val text: @Composable () -> String,
    val confirmButtonText: StringResource,
    val onClick: () -> Unit,
)