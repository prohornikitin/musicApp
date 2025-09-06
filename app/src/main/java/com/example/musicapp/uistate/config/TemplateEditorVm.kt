package com.example.musicapp.uistate.config

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.logic.impure.CardTemplatesEdit
import com.example.musicapp.domain.logic.impure.iface.storage.l2.read.TemplatesConfigRead
import com.example.musicapp.domain.logic.pure.parseTemplate
import com.example.musicapp.ui.annotateTemplate
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TemplateEditorVm @Inject constructor(
    private val cardTemplatesEdit: CardTemplatesEdit,
    private val templates: TemplatesConfigRead
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
        val (main, bottom) = templates.getTemplates()
        onMainTemplateChange(main.toString())
        onBottomTemplateChanged(bottom.toString())
    }

    fun onSave() {
        cardTemplatesEdit.update(
            parseTemplate(mainTemplate.text),
            parseTemplate(subTemplate.text),
        )
    }

    fun onMainTemplateChange(text: String) {
        mainTemplate = annotateTemplate(text, metaStyle)
    }

    fun onBottomTemplateChanged(text: String) {
        subTemplate = annotateTemplate(text, metaStyle)
    }
}