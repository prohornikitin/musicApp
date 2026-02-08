package afc.musicapp.domain.logic.impure.impl

import afc.musicapp.domain.logic.impure.impl.logger.Logger
import afc.musicapp.domain.logic.pure.logger.withClassTag


class EditorWithHistory<V, C : EditorWithHistory.Companion.Change<V, E>, E : Any>(
    val currentState: V,
    globalLogger: Logger,
    val maxHistoryLength: Int = Int.MAX_VALUE,
) : WithHistory {
    companion object {
        interface Change<T, E: Any> {
            fun applyTo(data: T): E?
            fun revert(data: T): E?
        }
    }

    private val logger = globalLogger.withClassTag(this)
    private val history = mutableListOf<C>()
    private var nextChangeIndex = 0


    fun makeChange(change: C): E? {
        return change.applyTo(currentState).also {
            recordToHistory(change)
        }
    }

    fun recordToHistory(change: C) {
        logger.debug("add to history: $change")
        when {
            nextChangeIndex == history.size -> {
                history.add(nextChangeIndex, change)
            }
            nextChangeIndex < history.size -> {
                if(history[nextChangeIndex] != change) {
                    for (i in history.size-1 downTo nextChangeIndex) {
                        history.removeAt(i)
                    }
                    history.add(change)
                }
            }
            else -> {
                throw IllegalStateException("nextChangeIndex $nextChangeIndex is greater than changes size ${history.size}")
            }
        }
        if (history.size > maxHistoryLength) {
            history.removeFirst()
        } else {
            nextChangeIndex++
        }
    }

    override fun undo() {
        if (!canUndo()) {
            return
        }
        history[nextChangeIndex - 1].revert(currentState)
        nextChangeIndex--
    }

    override fun redo() {
        if (!canRedo()) {
            return
        }
        history[nextChangeIndex].applyTo(currentState)
        nextChangeIndex++
    }

    override fun canUndo() = nextChangeIndex > 0
    override fun canRedo() = nextChangeIndex < history.size
    override fun clearHistory() = history.clear()
}