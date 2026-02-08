package afc.musicapp.domain.entities

enum class FontStyle(override val value: String) : SerializableEnum {
    Normal("Default"),
    Italic("Italic"),
    ;
}