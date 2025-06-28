package com.example.musicapp.uistate.config

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.logic.impure.iface.storage.read.TemplatesConfig
import com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed.TemplateUpdate
import com.example.musicapp.domain.logic.pure.parseTemplate
import com.example.musicapp.ui.annotateTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TemplateEditorVm @Inject constructor(
    private val templateUpdate: TemplateUpdate,
    templateStorage: TemplatesConfig,
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
        val value = templateStorage.getTemplates()
        onMainTextChange(value.main)
        onBottomTemplateChange(value.sub)
    }

    fun onMainTextChange(str: String) {
        mainTemplate = annotateTemplate(str, metaStyle)
    }

    fun onBottomTemplateChange(str: String) {
        subTemplate = annotateTemplate(str, metaStyle)
    }

    fun onSave() {
        templateUpdate.updateTemplates(
            parseTemplate(mainTemplate.text),
            parseTemplate(subTemplate.text),
        )
    }
}