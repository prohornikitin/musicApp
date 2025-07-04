package com.example.musicapp.uistate

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.storage.read.MetaStorage
import com.example.musicapp.domain.logic.impure.iface.storage.v2.read.SongThumbnailStorage
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class TagEditorVm @Inject constructor(
    private val tagsStorage: MetaStorage,
    private val thumbnailStorage: SongThumbnailStorage,
) : ViewModel() {
//    var thumbnail: ByteArray? by mutableStateOf(null)
    var tags: List<Pair<MetaKey, String>> by mutableStateOf(emptyList())
        private set

    fun loadForSong(id: SongId) {
        tags = tagsStorage.getAllFields(id)
    }

    fun changeTag(key: MetaKey, value: String) {
        tags += Pair(key, value)
    }

    fun delete(key: MetaKey, value: String) {
        tags -= Pair(key, value)
    }
}