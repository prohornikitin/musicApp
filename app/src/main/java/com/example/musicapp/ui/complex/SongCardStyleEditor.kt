package com.example.musicapp.ui.complex

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.graphics.drawable.toBitmap
import com.example.musicapp.domain.data.SongCardStyle.IconVisibility
import com.example.musicapp.R
import com.example.musicapp.domain.data.SongCardData
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.ui.reusable.FontConfigEditor
import com.example.musicapp.ui.reusable.IntSlider
import com.example.musicapp.ui.reusable.MenuOpenButton
import com.example.musicapp.ui.reusable.Spinner
import com.example.musicapp.ui.reusable.songcard.SongCard
import com.example.musicapp.uistate.config.SongCardStyleEditorVm
import com.example.musicapp.uistate.toByteArray

@Composable
fun SongCardStyleEditor(
    vm: SongCardStyleEditorVm,
    openDrawer: () -> Unit,
) {
    CommonSettingsScreen(
        topBar = {
            MenuOpenButton(openDrawer)
        },
        onSave = vm::onSave,
    ) {
        val iconBitmap =
            LocalContext.current.getDrawable(R.drawable.ic_launcher_background)?.toBitmap()
        Column(Modifier.verticalScroll(rememberScrollState())) {
            OutlinedCard {
                SongCard(
                    data = SongCardData(
                        id = SongId(1),
                        mainText = "Main text",
                        bottomText = "Bottom text",
                    ),
                    style = vm.config,
                )
            }
            OutlinedCard(
                modifier = Modifier.padding(top = 8.dp),
            ) {
                SongCard(
                    data = SongCardData(
                        id = SongId(2),
                        mainText = "Main text",
                        bottomText = "Bottom text",
                        iconBitmap = iconBitmap?.toByteArray(),
                    ),
                    style = vm.config,
                )
            }


            IntSlider(
                modifier = Modifier.padding(top = 8.dp),
                value = vm.config.iconSizeDp,
                onValueChange = { vm.config = vm.config.copy(iconSizeDp = it) },
                valueRange = 8..100,
                label = stringResource(R.string.icon_size),
            )

            Spinner(
                label = stringResource(R.string.icon_visibility),
                options = IconVisibility.entries,
                selected = vm.config.iconVisibility,
                onSelectionChanged = { vm.config = vm.config.copy(iconVisibility = it) },
                getName = {
                    stringResource(
                        when (it) {
                            IconVisibility.ShowIfAvailable -> R.string.icon_visibility__show_if_available
                            IconVisibility.Hide -> R.string.icon_visibility__hide
                            IconVisibility.Show -> R.string.icon_visibility__show
                        }
                    )
                }
            )

            OutlinedCard(Modifier.padding(top = 16.dp)) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(R.string.main_text),
                    fontWeight = FontWeight.Bold
                )
                FontConfigEditor(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    value = vm.config.mainFont,
                    onValueChange = { vm.config = vm.config.copy(mainFont = it) }
                )
            }

            OutlinedCard(Modifier.padding(top = 16.dp)) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(R.string.bottom_text),
                    fontWeight = FontWeight.Bold
                )
                FontConfigEditor(
                    modifier = Modifier.padding(horizontal = 4.dp),
                    value = vm.config.bottomFont,
                    onValueChange = { vm.config = vm.config.copy(bottomFont = it) }
                )
            }
        }
    }
}