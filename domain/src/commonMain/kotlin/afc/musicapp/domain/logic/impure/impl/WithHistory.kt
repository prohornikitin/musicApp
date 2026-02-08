package afc.musicapp.domain.logic.impure.impl

interface WithHistory {
    fun undo()
    fun redo()
    fun canUndo(): Boolean
    fun canRedo(): Boolean
    fun clearHistory()
}