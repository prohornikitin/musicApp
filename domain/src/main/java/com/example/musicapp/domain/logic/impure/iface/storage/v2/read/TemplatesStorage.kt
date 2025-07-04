package com.example.musicapp.domain.logic.impure.iface.storage.v2.read

import com.example.config.SongCardTemplates

interface TemplatesStorage : ObservableStorage<SongCardTemplates, TemplatesStorage.ChangeCallback> {
    interface ChangeCallback {
        fun onMainTemplateChange(text: String)
        fun onBottomTemplateChanged(text: String)
    }
}