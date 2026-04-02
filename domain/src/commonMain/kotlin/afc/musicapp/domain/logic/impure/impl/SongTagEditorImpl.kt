package afc.musicapp.domain.logic.impure.impl

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.logic.impure.iface.SongTagEditor
import afc.musicapp.domain.logic.impure.impl.logger.Logger
import afc.musicapp.domain.logic.impure.iface.SongTagEditor.Err

private typealias MutableTags = MutableList<Pair<MetaKey, String>>

class SongTagEditorImpl(
    state: MutableTags,
    globalLogger: Logger,
) : SongTagEditor {
    private var editor: EditorWithHistory<MutableTags, Change, Err> = EditorWithHistory(state, globalLogger)

    sealed interface Change : EditorWithHistory.Companion.Change<MutableTags, Err>{
        data class Add(val key: MetaKey): Change {
            override fun applyTo(data: MutableTags): Err? {
                data.add(Pair(key, ""))
                return null
            }

            override fun revert(data: MutableTags): Err? {
                val toRemove = data.indexOfLast { it.first == key }
                if (toRemove == -1) {
                    return Err.EditNotExistent
                }
                data.removeAt(toRemove)
                return null
            }
        }

        data class EditValue(val index: Int, val oldValue: String, val newValue: String): Change {
            override fun applyTo(data: MutableTags): Err? {
                if (index !in data.indices) {
                    return Err.EditNotExistent
                }
                data[index] = Pair(data[index].first, newValue)
                return null
            }

            override fun revert(data: MutableTags): Err? {
                if (index !in data.indices) {
                    return Err.EditNotExistent
                }
                data[index] = Pair(data[index].first, oldValue)
                return null
            }
        }

        data class Remove(val index: Int, val value: Pair<MetaKey, String>): Change {
            override fun applyTo(data: MutableTags): Err? {
                if (index !in data.indices) {
                    return Err.EditNotExistent
                }
                data.removeAt(index)
                return null
            }

            override fun revert(data: MutableTags): Err? {
                data.add(index, value)
                return null
            }
        }
    }

    override fun changeTag(index: Int, value: String): Err? {
        return editor.makeChange(Change.EditValue(index, editor.currentState[index].second, value))
    }

    override fun remove(index: Int): Err? {
        return editor.makeChange(Change.Remove(index, editor.currentState[index]))
    }

    override fun add(key: MetaKey): Err? {
        return editor.makeChange(Change.Add(key))
    }

    override fun undo() = editor.undo()
    override fun redo() = editor.redo()
    override fun canUndo() = editor.canUndo()
    override fun canRedo() = editor.canRedo()
    override fun clearHistory() = editor.clearHistory()
}