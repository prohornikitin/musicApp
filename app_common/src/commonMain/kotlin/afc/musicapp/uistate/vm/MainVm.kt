package afc.musicapp.uistate.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.viewModelScope
import afc.musicapp.domain.entities.SongCardData
import afc.musicapp.domain.entities.SongCardText
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.logic.impure.iface.Dispatchers
import afc.musicapp.domain.logic.impure.iface.SongCardDataRetrieve
import afc.musicapp.domain.logic.impure.iface.SongFileImport
import afc.musicapp.domain.logic.impure.iface.SongSearch
import afc.musicapp.domain.logic.pure.parseSearchQuery
import afc.musicapp.domain.recursiveSearchMusicFiles
import afc.musicapp.uistate.state.Key
import kotlinx.coroutines.launch
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import okio.FileSystem
import okio.Path.Companion.toPath
import kotlin.math.min


class MainVm constructor(
    private val search: SongSearch,
    private val songCardDataRepo: SongCardDataRetrieve,
    private val songFileImport: SongFileImport,
    private val musicDirsConfig: MusicDirsConfig,
    private val fileSystem: FileSystem,
    dispatchers: Dispatchers,
) : BaseVm(dispatchers) {
    companion object {
        const val PRELOAD_WHEN_AT_MOST_N_REMAINS = 10
        const val PRELOAD_CHUNK_SIZE = 15
    }

    private var songs = emptyList<SongId>()
    var cardsAndKeys by mutableStateOf(emptyList<Pair<Key, SongCardData>>())
        private set
    val cardPlaceholder = SongCardData(SongCardText("", ""))

    var searchQuery by mutableStateOf("")
        private set

    fun onSearchQueryChanged(newText: String) {
        if (searchQuery != newText) {
            searchQuery = newText
            search()
        }
    }

    init {
        loadIndicator {
            search()
        }
    }

    private fun search() {
        loadIndicator {
            val parsedQuery = parseSearchQuery(searchQuery)
            songs = search.search(parsedQuery)
            cardsAndKeys = songs.map { Pair(Key.of(it, false), cardPlaceholder) }
            loadMoreCards(0)
        }
    }

    fun needLoadMore(index: Int): Boolean {
        val shouldBeLoaded = cardsAndKeys.getOrNull(index + PRELOAD_WHEN_AT_MOST_N_REMAINS) ?: cardsAndKeys.lastOrNull()
        return shouldBeLoaded?.first?.isNotLoaded() == true
    }

    private val loadCardsMutex = Mutex()

    fun loadMoreCards(index: Int) = viewModelScope.launch(dispatchers.default) {
        loadCardsMutex.withLock {
            val new = cardsAndKeys.toMutableList()
            val lastLoadIndex = min(index + PRELOAD_CHUNK_SIZE,     new.size-1)
            println("load $index...$lastLoadIndex")
            for(i in index..lastLoadIndex) {
                val key = new[i].first
                if (key.isNotLoaded()) {
                    val id = key.toSongId()
                    new[i] = Pair(Key.of(id), songCardDataRepo.get(id) ?: cardPlaceholder)
                }
            }
            cardsAndKeys = new
        }
    }

    fun getPlaylist(from: Int): List<SongId> {
        return songs.subList(from, songs.size)
    }

    fun share(id: SongId) {
        TODO()
    }

    fun delete(id: SongId) {
        TODO()
    }

    fun onResume() {
        loadIndicator {
            if(songs.isEmpty()) {
                rescanForFiles()
            }
        }
    }

    private suspend fun rescanForFiles() {
        var dir = musicDirsConfig.getDir()
        if (dir == null) {
            //TODO: show dialog
            dir = "/sdcard/Music".toPath()
        }
        val audioFiles = recursiveSearchMusicFiles(dir, fileSystem)
        audioFiles.forEach {
            songFileImport.importIfSongNotExistsAlready(it)
        }
        search()
    }

    fun refresh() {
        loadIndicator {
            rescanForFiles()
        }
    }
}