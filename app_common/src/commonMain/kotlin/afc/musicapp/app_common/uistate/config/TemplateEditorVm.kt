package afc.musicapp.app_common.uistate.config

import afc.musicapp.app_common.ui.annotateTemplate
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import afc.musicapp.domain.entities.SongCardTemplates
import afc.musicapp.domain.logic.impure.iface.storage.TemplatesConfig
import afc.musicapp.domain.logic.pure.parseTemplate
import kotlinx.coroutines.launch
import kotlinx.coroutines.runBlocking

class TemplateEditorVm constructor(
    private val templatesConfig: TemplatesConfig,
    private val templates: TemplatesConfig
) : ViewModel() {
    var mainTemplate by mutableStateOf(AnnotatedString(""))
        private set

    var subTemplate by mutableStateOf(AnnotatedString(""))
        private set

    val metaStyle = SpanStyle(
        color = Color.Companion.Red,
        fontWeight = FontWeight.Companion.Bold,
    )

    init {
        runBlocking {
            val (main, bottom) = templates.getTemplates()
            onMainTemplateChange(main.toString())
            onBottomTemplateChanged(bottom.toString())
        }
    }

    fun onSave() {
        viewModelScope.launch {
            templatesConfig.updateTemplates(SongCardTemplates(
                parseTemplate(mainTemplate.text),
                parseTemplate(subTemplate.text),
            ))
        }
    }

    fun onMainTemplateChange(text: String) {
        mainTemplate = annotateTemplate(text, metaStyle)
    }

    fun onBottomTemplateChanged(text: String) {
        subTemplate = annotateTemplate(text, metaStyle)
    }
}