package afc.musicapp.ui

import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.AnnotatedString.Range
import afc.musicapp.domain.entities.FontFamily
import afc.musicapp.domain.entities.FontStyle
import afc.musicapp.domain.entities.RgbaColor
import afc.musicapp.domain.entities.Template
import afc.musicapp.domain.logic.pure.parseTemplate
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