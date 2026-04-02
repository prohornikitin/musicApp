package afc.musicapp.app_common.uistate.vm

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.logic.impure.iface.Dispatchers
import afc.musicapp.domain.logic.impure.iface.SongTagEditor
import afc.musicapp.domain.logic.impure.iface.storage.read.MetaRead
import afc.musicapp.domain.logic.impure.iface.storage.write.MetadataEdit
import afc.musicapp.domain.logic.impure.impl.SongTagEditorImpl
import afc.musicapp.domain.logic.impure.impl.logger.Logger
import afc.musicapp.domain.logic.pure.logger.withClassTag
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.error_edit_non_existent_tag
import org.jetbrains.compose.resources.Resource

class TagEditorVm constructor(
    private val tagsStorage: MetaRead,
    private val tagsEdit: MetadataEdit,
    globalLogger: Logger,
    dispatchers: Dispatchers,
) : BaseVm(dispatchers) {
    private val logger = globalLogger.withClassTag(this)

    private val errorRes by mutableStateOf<Resource?>(null)

    var song: SongId? = null
        set(value) {
            loadIndicator {
                field = value
                loadForSong(value)
            }
        }

//    var thumbnail: ByteArray? by mutableStateOf(null)
    val tags = mutableStateListOf<Pair<MetaKey, String>>()
    private var editor: SongTagEditor = SongTagEditorImpl(tags, logger)

    private suspend fun loadForSong(id: SongId?) {
        tags.clear()
        logger.debug("tags clear")
        if (id != null) {
            val new = tagsStorage.getAllFields(id)
            logger.debug { "tags load $new" }
            tags.addAll(new)
        }
        editor = SongTagEditorImpl(tags, logger)
        updateToolbarButtonsAvailability()
    }

    private fun errorText(err: SongTagEditor.Err): Resource {
        return when(err) {
            SongTagEditor.Err.EditNotExistent -> Res.string.error_edit_non_existent_tag
        }
    }


    fun addTag(): Resource? {
        return editor.add(MetaKey(dialogAddKey))?.let {
            dismissDialog()
            updateToolbarButtonsAvailability()
            errorText(it)
        }
    }



    fun changeTag(index: Int, value: String): Resource? {
        return editor.changeTag(index, value)?.let {
            updateToolbarButtonsAvailability()
            errorText(it)
        }
    }

    fun delete(index: Int): Resource? {
        return editor.remove(index)?.let {
            updateToolbarButtonsAvailability()
            errorText(it)
        }
    }

    fun undo() {
        editor.undo()
        updateToolbarButtonsAvailability()
    }
    fun redo() {
        editor.redo()
        updateToolbarButtonsAvailability()
    }

    var canUndo by mutableStateOf(false)
        private set
    var canRedo by mutableStateOf(false)
        private set

    fun updateToolbarButtonsAvailability() {
        canUndo = editor.canUndo()
        canRedo = editor.canRedo()
    }

    fun save() {
        song?.let {
            loadIndicator {
                tagsEdit.replaceAllTagsForSong(it, tags)
                editor.clearHistory()
                updateToolbarButtonsAvailability()
            }
        }
    }

    var dialogShown by mutableStateOf(false)
        private set

    var dialogAddKey by mutableStateOf("")

    fun showAddDialog() {
        dialogAddKey = ""
        dialogShown = true
    }

    fun dismissDialog() {
        dialogShown = false
    }
}