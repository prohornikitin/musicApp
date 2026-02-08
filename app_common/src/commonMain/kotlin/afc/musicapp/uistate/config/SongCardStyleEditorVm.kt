package afc.musicapp.uistate.config

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import afc.musicapp.domain.entities.SongCardStyle
import afc.musicapp.domain.logic.impure.impl.logger.Logger
import afc.musicapp.domain.logic.pure.logger.withClassTag

class SongCardStyleEditorVm constructor(defaultLogger: Logger) : ViewModel() {
    private val logger = defaultLogger.withClassTag(this)
    var config by mutableStateOf(SongCardStyle())

    fun onSave() {
        logger.debug("save")
    }
}