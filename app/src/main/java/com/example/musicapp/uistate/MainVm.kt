package com.example.musicapp.uistate

import android.os.Environment
import android.os.Environment.DIRECTORY_MUSIC
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.musicapp.data.SongOption
import com.example.musicapp.data.SongOption.*
import com.example.musicapp.domain.data.SongId
import com.example.musicapp.domain.logic.impure.iface.storage.read.GeneratedTemplatesStorage
import com.example.musicapp.domain.logic.impure.iface.MetaParser
import com.example.musicapp.domain.logic.impure.iface.SongSearch
import com.example.musicapp.domain.logic.impure.iface.storage.read.SongThumbnailStorage
import com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed.SongThumbnailUpdate
import com.example.musicapp.domain.logic.impure.iface.storage.read.MetaKeyMapping
import com.example.musicapp.domain.logic.impure.iface.storage.write.noIntegrity.MetaDbUpdate
import com.example.musicapp.domain.isAudio
import com.example.musicapp.domain.recursiveSearchFiles
import com.example.musicapp.domain.data.SongCardData
import com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed.SongAdd
import com.example.musicapp.domain.logic.impure.iface.storage.write.integrityGuaranteed.SongRemove
import com.example.musicapp.domain.logic.pure.parseSearchQuery
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject
import kotlin.coroutines.CoroutineContext


@HiltViewModel
class MainVm @Inject constructor(
    private val songAdd: SongAdd,
    private val search: SongSearch,
    private val generatedTemplatesStorage: GeneratedTemplatesStorage,
    private val metaKeyMapping: MetaKeyMapping,
    private val metaParser: MetaParser,
    private val thumbnailStorageEditor: SongThumbnailUpdate,
    private val metaDbUpdate: MetaDbUpdate,
    private val iconStorage: SongThumbnailStorage,
    private val songRemove: SongRemove,
) : ViewModel() {
    private var allSongs by mutableStateOf(search.listSongs())
    var afterSearch by mutableStateOf(emptyList<SongCardData>())
        private set

    var isLoading by mutableStateOf(false)

    var searchQuery by mutableStateOf("")
        private set

    fun onSearchQueryChanged(newText: String) {
        searchQuery = newText
        onSearch()
    }

    private fun load(context: CoroutineContext = Dispatchers.IO, block: () -> Unit) {
        viewModelScope.launch(context) {
            isLoading = true
            try {
                block()
            } finally {
                isLoading = false
            }
        }
    }

    private fun loadData(id: SongId): SongCardData {
        val (main, sub) = generatedTemplatesStorage.getSongCardText(id)
        val icon = iconStorage.getIcon(id)
        return SongCardData(
            id = id,
            mainText = main,
            bottomText = sub,
            iconBitmap = icon
        )
    }

    fun onSearch() {
        load {
            search(searchQuery)
        }
    }

    private fun search(query: String) {
        val parsed = parseSearchQuery(query)
        afterSearch = search.search(
            allSongs,
            parsed,
        ).map(::loadData)
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
        return allSongs.dropWhile { it != id }
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
        songRemove.remove(id)
    }

    fun onResume() {
        load {
            var songsFromDb = search.listSongs()
            if(songsFromDb.isEmpty()) {
                reloadMusicFromFiles()
                songsFromDb = search.listSongs()
            }
            allSongs = songsFromDb
            search("")
        }
    }

    private fun reloadMusicFromFiles() {
//        Environment.getExternalStorageDirectory()
        val musicPath = Environment.getExternalStoragePublicDirectory(DIRECTORY_MUSIC)
        val audioFiles = recursiveSearchFiles(musicPath)
            .filter { isAudio(it) }
        val tagsMapping by lazy { metaKeyMapping.getMetaKeyMappings() }
        audioFiles.forEach {
            val fullMeta by lazy { metaParser.getFullMetaFromFile(it, tagsMapping) }
            val id = songAdd.addNewIfNotExists(it)
            if(id == null) {
                return@forEach
            }
            metaDbUpdate.updateMetadata(id, fullMeta.properties)
            fullMeta.icon?.let { thumbnailStorageEditor.saveIcon(id, it) }
        }
        search("")
    }

    fun reload() {
        load {
            reloadMusicFromFiles()
        }
    }
}