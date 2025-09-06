package com.example.musicapp.domain.logic.impure.iface.storage.l2.read

import com.example.musicapp.domain.data.SongCardTemplates

interface TemplatesConfigRead {
    fun getTemplates(): SongCardTemplates

    fun interface ChangeListener {
        fun onTemplatesChange(new: SongCardTemplates)
    }

    val listeners: MutableCollection<ChangeListener>
}