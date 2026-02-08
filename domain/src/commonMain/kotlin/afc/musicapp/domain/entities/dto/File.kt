package afc.musicapp.domain.entities.dto

data class File(
    val path: String,
    val hash: String,
    val modificationDate: String
)