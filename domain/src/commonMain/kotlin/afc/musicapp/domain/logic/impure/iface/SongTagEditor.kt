package afc.musicapp.domain.logic.impure.iface

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.logic.impure.impl.WithHistory

interface SongTagEditor : WithHistory {
    enum class Err {
        EditNotExistent,
    }

    fun changeTag(index: Int, value: String): Err?
    fun remove(index: Int): Err?
    fun add(key: MetaKey): Err?
}