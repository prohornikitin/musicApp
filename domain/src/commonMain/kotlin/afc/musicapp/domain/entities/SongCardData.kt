package afc.musicapp.domain.entities

data class SongCardData(
    val text: SongCardText,
    val icon: ByteArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as SongCardData

        if (text != other.text) return false
        if (!icon.contentEquals(other.icon)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = text.hashCode()
        result = 31 * result + (icon?.contentHashCode() ?: 0)
        return result
    }
}