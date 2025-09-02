package com.example.musicapp.domain.logic.pure

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SongId

fun mergeMultiValueMetas(meta: List<Pair<MetaKey, String>>, delimiter: String): Map<MetaKey, String> {
    val result = mutableMapOf<MetaKey, String>()
    meta.forEach {
        val key = it.first
        val value = it.second
        val previousValue = result[key]
        if (previousValue == null) {
            result[key] = value
        } else {
            result[key] = previousValue + delimiter + value
        }
    }
    return result
}

fun List<Triple<SongId, MetaKey, String>>.toMetasBySong(): Map<SongId, List<Pair<MetaKey, String>>> {
    return this
        .groupBy { it.first }
        .mapValues { it.value.map { Pair(it.second, it.third) } }
}