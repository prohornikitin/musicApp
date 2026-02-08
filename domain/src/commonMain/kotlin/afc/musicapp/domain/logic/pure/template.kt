package afc.musicapp.domain.logic.pure

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SongCardTemplates
import afc.musicapp.domain.entities.SongCardText
import afc.musicapp.domain.entities.Template


enum class State {
    INITIAL,
    IN_TEMPLATE,
}

fun parseTemplate(raw: String): Template {
    var tokenStart = 0
    var lastStr = StringBuilder()
    val entries = mutableListOf<Template.Entry>()
    var state = State.INITIAL
    ("$raw#").forEachIndexed { i,c ->
        when(state) {
            State.INITIAL -> {
                if (c == '#') {
                    if (lastStr.isNotEmpty()) {
                        entries.add(Template.PlainText(lastStr.toString(), tokenStart..i-1))
                    }
                    tokenStart = i
                    state = State.IN_TEMPLATE
                    lastStr.clear()
                } else {
                    lastStr.append(c)
                }
            }
            State.IN_TEMPLATE -> {
                if (!c.isLetter() && c != '_') {
                    if (lastStr.isNotEmpty()) {
                        entries.add(Template.MetaField(MetaKey(lastStr.toString()), tokenStart..i-1))
                    }
                    tokenStart = i
                    lastStr.clear()
                    if(c != '#') {
                        state = State.INITIAL
                        lastStr.append(c)
                    }
                } else {
                    lastStr.append(c)
                }
            }
        }
    }
    return Template(entries)
}


fun applyTemplate(template: Template, meta: Map<MetaKey, String>): String {
    return template.entries
        .map {
            when(it) {
                is Template.MetaField -> (meta[it.key] ?: "")
                is Template.PlainText -> it.text
            }
        }
        .fold(StringBuilder()) { acc, s ->
            acc.append(s)
        }
        .toString()
}

fun applyTemplates(templates: SongCardTemplates, meta: Map<MetaKey, String>): SongCardText {
    return SongCardText(
        applyTemplate(templates.main, meta),
        applyTemplate(templates.bottom, meta),
    )
}