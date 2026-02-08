package afc.musicapp.ui.reusable

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.open_menu
import org.jetbrains.compose.resources.stringResource


@Composable
fun MenuOpenButton(onClick: () -> Unit) {
    IconButton(onClick) {
        Icon(
            imageVector = Icons.Default.Menu,
            contentDescription = stringResource(Res.string.open_menu),
        )
    }
}