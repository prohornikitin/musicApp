package afc.musicapp.app_common.uistate.state

import afc.musicapp.domain.entities.SongCardData

data class MiniPlayer(
    val song: SongCardData?,
    val isPlaying: Boolean,
)