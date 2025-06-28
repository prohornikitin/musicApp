package com.example.musicapp.domain.data

@JvmInline
value class MetaKey(val raw: String) {
    object Raw {
        const val TITLE = "TITLE"
        const val ARTIST = "ARTIST"
        const val ALBUM = "ALBUM"
        const val ALBUM_ARTIST = "ALBUM_ARTIST"
    }
    companion object {
        val TITLE = MetaKey(Raw.TITLE)
        val ARTIST = MetaKey(Raw.ARTIST)
        val ALBUM = MetaKey(Raw.ALBUM)
        val ALBUM_ARTIST = MetaKey(Raw.ALBUM_ARTIST)
    }
}