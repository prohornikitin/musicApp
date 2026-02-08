package afc.musicapp.domain.entities

import kotlinx.serialization.Serializable

@Serializable
data class SongCardText(
    val main: String = "",
    val sub: String = "",
)