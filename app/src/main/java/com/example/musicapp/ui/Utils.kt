package com.example.musicapp.ui

import androidx.compose.material3.DrawerState
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.AnnotatedString.Range
import com.example.config.FontFamily
import com.example.config.FontStyle
import com.example.config.RgbaColor
import com.example.musicapp.domain.data.Template
import com.example.musicapp.domain.logic.pure.parseTemplate
import androidx.compose.ui.text.font.FontFamily as ComposeFontFamily
import androidx.compose.ui.text.font.FontStyle as ComposeFontStyle

fun FontFamily.toComposeFontFamily(): ComposeFontFamily = when(this) {
    FontFamily.Default -> ComposeFontFamily.Default
    FontFamily.Monospace -> ComposeFontFamily.Monospace
    FontFamily.Serif -> ComposeFontFamily.Serif
    FontFamily.Cursive -> ComposeFontFamily.Cursive
    FontFamily.SansSerif -> ComposeFontFamily.SansSerif
}

fun FontStyle.toComposeFontStyle(): ComposeFontStyle = when(this) {
    FontStyle.Normal -> ComposeFontStyle.Normal
    FontStyle.Italic -> ComposeFontStyle.Italic
}

fun RgbaColor.toComposeColor() = Color(this.raw)


fun annotateTemplate(templateText: String, metaStyle: AnnotatedString.Annotation) = AnnotatedString(
    templateText,
    parseTemplate(templateText).entries.mapNotNull {
        when(it) {
            is Template.MetaField -> Range<AnnotatedString.Annotation>(
                metaStyle,
                it.sourceIndexes.start,
                it.sourceIndexes.endInclusive + 1,
            )
            is Template.PlainText -> null
        }
    }
)