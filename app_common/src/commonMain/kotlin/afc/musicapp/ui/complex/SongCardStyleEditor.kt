package afc.musicapp.ui.complex

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.OutlinedCard
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import afc.musicapp.domain.entities.SongCardStyle.IconVisibility
import afc.musicapp.domain.entities.SongCardData
import afc.musicapp.domain.entities.SongCardText
import afc.musicapp.ui.reusable.FontConfigEditor
import afc.musicapp.ui.reusable.IntSlider
import afc.musicapp.ui.reusable.MenuOpenButton
import afc.musicapp.ui.reusable.Spinner
import afc.musicapp.ui.reusable.songcard.SongCard
import afc.musicapp.uistate.config.SongCardStyleEditorVm
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.bottom_text
import musicapp.app_common.generated.resources.icon_size
import musicapp.app_common.generated.resources.icon_visibility
import musicapp.app_common.generated.resources.icon_visibility__hide
import musicapp.app_common.generated.resources.icon_visibility__show
import musicapp.app_common.generated.resources.icon_visibility__show_if_available
import musicapp.app_common.generated.resources.main_text
import org.jetbrains.compose.resources.stringResource

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
//        val iconBitmap = getDrawablePath(LocalContext.current, R.drawable.ic_launcher_background)
        Column(Modifier.verticalScroll(rememberScrollState())) {
            OutlinedCard {
                SongCard(
                    SongCardData(
                        text = SongCardText(
                            main = "Main text",
                            sub = "Bottom text",
                        ),
                    ),
                    style = vm.config,
                )
            }
            OutlinedCard(
                modifier = Modifier.padding(top = 8.dp),
            ) {
                SongCard(
                    data = SongCardData(
                        text = SongCardText(
                            main = "Main text",
                            sub = "Bottom text",
                        ),
                    ),
                    style = vm.config,
                )
            }


            IntSlider(
                modifier = Modifier.padding(top = 8.dp),
                value = vm.config.iconSizeDp,
                onValueChange = { vm.config = vm.config.copy(iconSizeDp = it) },
                valueRange = 8..100,
                label = stringResource(Res.string.icon_size),
            )

            Spinner(
                label = stringResource(Res.string.icon_visibility),
                options = IconVisibility.entries,
                selected = vm.config.iconVisibility,
                onSelectionChanged = { vm.config = vm.config.copy(iconVisibility = it) },
                getName = {
                    stringResource(
                        when (it) {
                            IconVisibility.ShowIfAvailable -> Res.string.icon_visibility__show_if_available
                            IconVisibility.Hide -> Res.string.icon_visibility__hide
                            IconVisibility.Show -> Res.string.icon_visibility__show
                        }
                    )
                }
            )

            OutlinedCard(Modifier.padding(top = 16.dp)) {
                Text(
                    modifier = Modifier.padding(8.dp),
                    text = stringResource(Res.string.main_text),
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
                    text = stringResource(Res.string.bottom_text),
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