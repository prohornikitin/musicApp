package afc.musicapp.app_common.uistate.vm

import afc.musicapp.app_common.uistate.state.Key
import afc.musicapp.app_common.uistate.state.SimpleDialogConfig
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import afc.musicapp.domain.entities.SongCardData
import afc.musicapp.domain.entities.SongCardText
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.logic.impure.iface.Dispatchers
import afc.musicapp.domain.logic.impure.iface.storage.read.SongCardDataRead
import afc.musicapp.domain.logic.impure.iface.SongFileImport
import afc.musicapp.domain.logic.impure.iface.SongRemove
import afc.musicapp.domain.logic.impure.iface.SongSearch
import afc.musicapp.domain.logic.impure.iface.storage.MusicDirConfig
import afc.musicapp.domain.logic.pure.parseSearchQuery
import afc.musicapp.domain.recursiveSearchMusicFiles
import kotlinx.coroutines.sync.Mutex
import kotlinx.coroutines.sync.withLock
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.are_you_sure
import musicapp.app_common.generated.resources.delete
import musicapp.app_common.generated.resources.do_you_want_to_delete_it
import okio.FileSystem
import okio.Path.Companion.toPath
import org.jetbrains.compose.resources.stringResource
import kotlin.math.min


class MainVm constructor(
    private val search: SongSearch,
    private val songCardDataRepo: SongCardDataRead,
    private val songFileImport: SongFileImport,
    private val musicDirConfig: MusicDirConfig,
    private val fileSystem: FileSystem,
    private val songRemove: SongRemove,
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

    var dialog by mutableStateOf<SimpleDialogConfig?>(null)

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
            cardsAndKeys = songs.map { Pair(Key.Companion.of(it, false), cardPlaceholder) }
            loadMoreCards(0)
        }
    }

    fun needLoadMore(index: Int): Boolean {
        val shouldBeLoaded = cardsAndKeys.getOrNull(index + PRELOAD_WHEN_AT_MOST_N_REMAINS) ?: cardsAndKeys.lastOrNull()
        return shouldBeLoaded?.first?.isNotLoaded() == true
    }

    private val loadCardsMutex = Mutex()

    fun loadMoreCards(index: Int) = launchOnDefault {
        loadCardsMutex.withLock {
            val new = cardsAndKeys.toMutableList()
            val lastLoadIndex = min(index + PRELOAD_CHUNK_SIZE,     new.size-1)
            for(i in index..lastLoadIndex) {
                val key = new[i].first
                if (key.isNotLoaded()) {
                    val id = key.toSongId()
                    new[i] = Pair(Key.Companion.of(id), songCardDataRepo.get(id) ?: cardPlaceholder)
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

    fun deleteFile(id: SongId) {
        launchOnDefault {
            val name = songCardDataRepo.get(id)?.text?.main ?: "INVALID_SONG"
            dialog = SimpleDialogConfig(
                title = Res.string.are_you_sure,
                text = { stringResource(Res.string.do_you_want_to_delete_it, name) },
                confirmButtonText = Res.string.delete,
            ) {
                launchOnDefault {
                    songRemove.remove(id)
                }
                dialog = null
            }
        }
    }

    fun onResume() {
        loadIndicator {
            if(songs.isEmpty()) {
                rescanForFiles()
            }
        }
    }

    private suspend fun rescanForFiles() {
        var dir = musicDirConfig.getDir()
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