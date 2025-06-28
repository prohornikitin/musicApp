package com.example.musicapp.domain.data

data class FullMeta(
    val properties: Map<MetaKey, String> = emptyMap(),
    val icon: ByteArray? = null,
) {
    override fun equals(other: Any?): Boolean {
        if (this === other) return true
        if (javaClass != other?.javaClass) return false

        other as FullMeta

        if (properties != other.properties) return false
        if (!icon.contentEquals(other.icon)) return false

        return true
    }

    override fun hashCode(): Int {
        var result = properties.hashCode()
        result = 31 * result + icon.contentHashCode()
        return result
    }
}