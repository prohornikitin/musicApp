package afc.musicapp.uistate.state

import afc.musicapp.domain.entities.SongId
import kotlin.jvm.JvmInline

@JvmInline
value class Key(val value: Long) {
    companion object {
        fun of(id: SongId, loaded: Boolean = true): Key {
            val value = if (loaded) {
                id.raw
            } else {
                - id.raw - 1
            }
            return Key(value)
        }
    }

    fun isLoaded() = value >= 0
    fun isNotLoaded() = value < 0

    fun toSongId(): SongId {
        return if (isLoaded()) {
            SongId(value)
        } else {
            SongId(- value - 1)
        }
    }
}