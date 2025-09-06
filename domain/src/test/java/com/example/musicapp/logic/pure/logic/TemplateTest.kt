package com.example.musicapp.logic.pure.logic

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.Template
import com.example.musicapp.domain.logic.pure.applyTemplate
import com.example.musicapp.domain.logic.pure.parseTemplate
import org.junit.Test
import org.junit.Assert.*

class TemplateTest {
    @Test
    fun testParse_textOnly() {
        assertEquals(
            Template(listOf(
                Template.PlainText("main", 0..3)
            )),
            parseTemplate(
                "main",
            )
        )
    }

    @Test
    fun testParse_metaOnly() {
        assertEquals(
            Template(listOf(
                Template.MetaField(MetaKey("TITLE"), 0..5),
                Template.MetaField(MetaKey("ALBUM"), 6..11),
            )),
            parseTemplate(
                "#TITLE#ALBUM",
            )
        )
    }

    @Test
    fun testParse_empty() {
        assertEquals(
            Template(listOf()),
            parseTemplate(
                "",
            )
        )
    }

    @Test
    fun testParse_regular() {
        assertEquals(
            Template(listOf(
                Template.MetaField(MetaKey("TITLE"), 0..5),
                Template.PlainText(" - ", 6..8),
                Template.MetaField(MetaKey("ALBUM"), 9..14),
            )),
            parseTemplate(
                "#TITLE - #ALBUM",
            )
        )
    }

    @Test
    fun toString_regular() {
        assertEquals(
            Template(listOf(
                Template.MetaField(MetaKey("TITLE"), 0..6),
                Template.PlainText("|", 6..7),
                Template.MetaField(MetaKey("ALBUM"), 7..13),
            )).toString(),
            "#TITLE|#ALBUM",
        )
    }

    @Test
    fun toString_textOnly() {
        assertEquals(
            Template(listOf(
                Template.PlainText("main", 0..4)
            )).toString(),
            "main",
        )
    }

    @Test
    fun toString_metaOnly() {
        assertEquals(
            Template(listOf(
                Template.MetaField(MetaKey("TITLE"), 0..6),
                Template.MetaField(MetaKey("ALBUM"), 6..12),
            )).toString(),
            "#TITLE#ALBUM",
        )
    }

    @Test
    fun toString_empty() {
        assertEquals(
            Template(listOf()).toString(),
            "",
        )
    }

    @Test
    fun usedKeys() {
        assertEquals(
            Template(listOf(
                Template.MetaField(MetaKey("TITLE"), 0..6),
                Template.PlainText("|", 6..7),
                Template.MetaField(MetaKey("ALBUM"), 7..13),
            )).getUsedKeys(),
            setOf(MetaKey.TITLE, MetaKey.ALBUM)
        )
    }

    @Test
    fun applyTemplate_noRequiredMeta() {
        assertEquals(
            applyTemplate(parseTemplate("#TITLE#ALBUM"), mapOf(
                MetaKey.TITLE to "TITLE",
            )),
            "TITLE"
        )
    }
}

