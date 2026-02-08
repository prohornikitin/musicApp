package afc.musicapp.uistate.vm

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateMapOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.logic.impure.iface.Dispatchers
import afc.musicapp.domain.logic.impure.iface.MergedMetaRead
import afc.musicapp.domain.logic.impure.iface.SongTagEditor
import afc.musicapp.domain.logic.impure.iface.storage.transactional.write.MetadataEdit
import afc.musicapp.domain.logic.impure.impl.SongTagEditorImpl
import afc.musicapp.domain.logic.impure.impl.logger.Logger
import afc.musicapp.domain.logic.pure.logger.withClassTag
import kotlinx.coroutines.runBlocking
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.error_edit_non_existent_tag
import musicapp.app_common.generated.resources.error_tag_already_exists
import org.jetbrains.compose.resources.Resource

class TagEditorVm constructor(
    private val tagsStorage: MergedMetaRead,
    private val tagsEdit: MetadataEdit,
    globalLogger: Logger,
    dispatchers: Dispatchers,
) : BaseVm(dispatchers) {
    private val logger = globalLogger.withClassTag(this)

    private val errorRes by mutableStateOf<Resource?>(null)

    var song: SongId? = null
        set(value) {
            field = value
            loadForSong(value)
        }

//    var thumbnail: ByteArray? by mutableStateOf(null)
    val tags = mutableStateMapOf<MetaKey, String>()

    private val editor: SongTagEditor = SongTagEditorImpl(tags, logger)

    private fun loadForSong(id: SongId?) {
        tags.clear()
        if (id != null) runBlocking {
            tags.putAll(tagsStorage.getAllFieldsMerged(id))
        }
        updateToolbarButtonsAvailability()
    }

    private fun errorText(err: SongTagEditor.Err): Resource {
        return when(err) {
            SongTagEditor.Err.AddAlreadyExistent -> Res.string.error_tag_already_exists
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



    fun changeTag(key: MetaKey, value: String): Resource? {
        return editor.changeTag(key, value)?.let {
            updateToolbarButtonsAvailability()
            errorText(it)
        }
    }

    fun delete(key: MetaKey): Resource? {
        return editor.remove(key)?.let {
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
                tagsEdit.replaceFieldsFromMerged(it, tags)
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