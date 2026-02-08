package afc.musicapp.domain.logic.impure.impl

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.logic.impure.iface.SongTagEditor
import afc.musicapp.domain.logic.impure.impl.logger.Logger
import afc.musicapp.domain.logic.impure.iface.SongTagEditor.Err

private typealias MutableTags = MutableMap<MetaKey, String>

class SongTagEditorImpl(
    state: MutableMap<MetaKey, String>,
    globalLogger: Logger,
) : SongTagEditor {
    private var editor: EditorWithHistory<MutableTags, Change, Err> = EditorWithHistory(state, globalLogger)

    sealed interface Change : EditorWithHistory.Companion.Change<MutableTags, Err>{
        data class Add(val key: MetaKey): Change {
            override fun applyTo(data: MutableTags): SongTagEditor.Err? {
                if (data.containsKey(key)) {
                    return SongTagEditor.Err.AddAlreadyExistent
                }
                data[key] = ""
                return null
            }

            override fun revert(data: MutableTags): SongTagEditor.Err? {
                if (!data.containsKey(key)) {
                    return SongTagEditor.Err.EditNotExistent
                }
                data.remove(key)
                return null
            }
        }

        data class EditValue(val key: MetaKey, val oldValue: String, val newValue: String): Change {
            override fun applyTo(data: MutableTags): SongTagEditor.Err? {
                if (!data.containsKey(key)) {
                    return SongTagEditor.Err.EditNotExistent
                }
                data[key] = newValue
                return null
            }

            override fun revert(data: MutableTags): SongTagEditor.Err? {
                if (!data.containsKey(key)) {
                    return SongTagEditor.Err.EditNotExistent
                }
                data[key] = oldValue
                return null
            }
        }

        data class Remove(val key: MetaKey, val oldValue: String): Change {
            override fun applyTo(data: MutableMap<MetaKey, String>): SongTagEditor.Err? {
                if (!data.containsKey(key)) {
                    return SongTagEditor.Err.EditNotExistent
                }
                data.remove(key)
                return null
            }

            override fun revert(data: MutableTags): SongTagEditor.Err? {
                if (data.containsKey(key)) {
                    return SongTagEditor.Err.AddAlreadyExistent
                }
                data[key] = oldValue
                return null
            }
        }
    }

    override fun changeTag(key: MetaKey, value: String): SongTagEditor.Err? {
        return editor.makeChange(Change.EditValue(key, editor.currentState[key]!!, value))
    }

    override fun remove(key: MetaKey): SongTagEditor.Err? {
        return editor.makeChange(Change.Remove(key, editor.currentState[key]!!))
    }

    override fun add(key: MetaKey): SongTagEditor.Err? {
        return editor.makeChange(Change.Add(key))
    }

    override fun undo() = editor.undo()
    override fun redo() = editor.redo()
    override fun canUndo() = editor.canUndo()
    override fun canRedo() = editor.canRedo()
    override fun clearHistory() = editor.clearHistory()
}