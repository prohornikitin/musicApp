package afc.musicapp.domain.entities

data class Metadata(
    val properties: Map<MetaKey, String> = emptyMap(),
    val icon: ByteArray? = null,
) {

    override fun hashCode(): Int {
        var result = properties.hashCode()
        result = 31 * result + icon.contentHashCode()
        return result
    }

    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (other == null || this::class != other::class) return false

        other as Metadata

        if (properties != other.properties) return false
        if (!icon.contentEquals(other.icon)) return false

        return true
    }
}