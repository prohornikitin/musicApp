package afc.musicapp.uistate.state

import afc.musicapp.domain.entities.SongCardData
import afc.musicapp.domain.entities.SyncedLyrics
import kotlin.time.ComparableTimeMark

data class PlayerScreen(
    val song: SongCardData?,
    val fullLyrics: String?,
    val time: ComparableTimeMark,
    val syncedLyrics: SyncedLyrics.Item?,
    val isPlaying: Boolean,
)