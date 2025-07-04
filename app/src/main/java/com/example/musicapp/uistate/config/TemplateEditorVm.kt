package com.example.musicapp.uistate.config

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.logic.impure.iface.storage.v2.read.TemplatesStorage
import com.example.musicapp.domain.logic.pure.parseTemplate
import com.example.musicapp.ui.annotateTemplate

class TemplateEditorVm constructor(
    private val updateCardTemplates: UpdateCardTemplates,
    private val templates: TemplatesStorage
) : ViewModel(), TemplatesStorage.ChangeCallback {
    var mainTemplate by mutableStateOf(AnnotatedString(""))
        private set

    var subTemplate by mutableStateOf(AnnotatedString(""))
        private set

    val metaStyle = SpanStyle(
        color = Color.Companion.Red,
        fontWeight = FontWeight.Companion.Bold,
    )

    init {
        templates.addChangeCallback(this)
        addCloseable { templates.removeChangeCallback(this) }

        val (main, bottom) = templates.get()
        onMainTemplateChange(main)
        onBottomTemplateChanged(bottom)
    }

    fun onSave() {
        updateCardTemplates(
            parseTemplate(mainTemplate.text),
            parseTemplate(subTemplate.text),
        )
    }

    override fun onMainTemplateChange(text: String) {
        mainTemplate = annotateTemplate(text, metaStyle)
    }

    override fun onBottomTemplateChanged(text: String) {
        subTemplate = annotateTemplate(text, metaStyle)
    }
}