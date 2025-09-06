package com.example.musicapp.domain.logic.impure

import com.example.musicapp.domain.data.Template

interface CardTemplatesEdit {
    fun update(main: Template, sub: Template)
}