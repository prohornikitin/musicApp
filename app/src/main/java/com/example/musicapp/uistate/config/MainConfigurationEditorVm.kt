package com.example.musicapp.uistate.config

import androidx.annotation.StringRes
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.stringResource
import androidx.lifecycle.ViewModel
import com.example.musicapp.R
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MainConfigurationEditorVm @Inject constructor(

) : ViewModel() {
    enum class SubMenu(@StringRes val res: Int) {
        Templates(R.string.settings_template),
        SongCardStyle(R.string.settings_card_style),
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