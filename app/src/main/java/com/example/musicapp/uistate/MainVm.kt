package com.example.musicapp.uistate

import android.os.Environment
import android.os.Environment.DIRECTORY_MUSIC
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.SongOption
import com.example.musicapp.data.SongOption.*
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.SongSearch
import com.example.musicapp.domain.isAudio
import com.example.musicapp.domain.recursiveSearchFiles
import com.example.musicapp.domain.logic.impure.iface.SongFileLoad
import com.example.musicapp.domain.logic.impure.iface.storage.v2.read.SongCardDataStorage
import com.example.musicapp.domain.logic.pure.parseSearchQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


@HiltViewModel
class MainVm @Inject constructor(
    private val search: SongSearch,
    private val songCardDataStorage: SongCardDataStorage,
    private val songFileLoad: SongFileLoad,
) : ViewModel() {
    private var loadingObjects by mutableIntStateOf(0)
    val isLoading by derivedStateOf { loadingObjects == 0 }

    var songs by mutableStateOf(emptyList<SongId>())
    val cards by derivedStateOf { songCardDataStorage.get(songs) }
    var searchQuery by mutableStateOf("")
        private set

    fun onSearchQueryChanged(newText: String) {
        searchQuery = newText
        search()
    }

    private fun search() {
        load {
            val parsedQuery = parseSearchQuery(searchQuery)
            songs = search.search(parsedQuery)
        }
    }

    private fun load(context: CoroutineContext = Dispatchers.IO, block: () -> Unit) {
        viewModelScope.launch(context) {
            synchronized(this@MainVm) {
                loadingObjects++
            }
            try {
                block()
            } finally {
                synchronized(this@MainVm) {
                    loadingObjects--
                }
            }
        }
    }

    fun execSongOption(id: SongId, option: SongOption) {
        val func = when(option) {
            EditMeta -> ::editMeta
            Details -> ::details
            Delete -> ::delete
            Share -> ::share
        }
        func(id)
    }

    fun getPlaylistFrom(id: SongId): List<SongId> {
        return songs.dropWhile { it != id }
    }

    private fun editMeta(id: SongId) {
        TODO()
    }

    private fun details(id: SongId) {
        TODO()
    }

    private fun share(id: SongId) {
        TODO()
    }

    private fun delete(id: SongId) {
        TODO()
    }

    fun onResume() {
        load {
            if(songs.isEmpty()) {
                rescanForFiles()
                search()
            }
        }
    }

    private fun rescanForFiles() {
        val musicPath = Environment.getExternalStoragePublicDirectory(DIRECTORY_MUSIC)
        val audioFiles = recursiveSearchFiles(musicPath)
            .filter { isAudio(it) }
        audioFiles.forEach {
            songFileLoad.loadNewIfNotExists(it)
        }
    }

    fun reload() {
        load {
            rescanForFiles()
            search()
        }
    }
}