package com.example.musicapp.domain.data

data class SearchQuery(
    val metaPrefixes: Map<MetaKey, String>,
    val plain: String,
) {

//    init {
//        assert(metaPrefixes.keys.all { !it.raw.all{ it.isLetter() } })
//    }
    fun isEmpty() = plain.isEmpty() && metaPrefixes.isEmpty()
}