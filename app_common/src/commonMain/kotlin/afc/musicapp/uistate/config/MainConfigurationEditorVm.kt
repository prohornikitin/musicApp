package afc.musicapp.uistate.config

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.settings_card_style
import musicapp.app_common.generated.resources.settings_template
import org.jetbrains.compose.resources.StringResource
import org.jetbrains.compose.resources.stringResource

class MainConfigurationEditorVm() : ViewModel() {
    enum class SubMenu(val res: StringResource) {
        Templates(Res.string.settings_template),
        SongCardStyle(Res.string.settings_card_style),
        ;

        @Composable
        fun title() = stringResource(res)
    }

    var selectedSubMenu by mutableStateOf(SubMenu.entries[0])
        private set

    fun onSubMenuClick(menu: SubMenu) {
        selectedSubMenu = menu
    }
}