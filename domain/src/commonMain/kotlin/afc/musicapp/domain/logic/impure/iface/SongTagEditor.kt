package afc.musicapp.domain.logic.impure.iface

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.logic.impure.impl.WithHistory

interface SongTagEditor : WithHistory {
    enum class Err {
        AddAlreadyExistent,
        EditNotExistent,
    }

    fun changeTag(key: MetaKey, value: String): Err?
    fun remove(key: MetaKey): Err?
    fun add(key: MetaKey): Err?
}