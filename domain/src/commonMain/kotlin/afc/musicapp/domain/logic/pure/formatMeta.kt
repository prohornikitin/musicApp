package afc.musicapp.domain.logic.pure

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SongId

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

fun splitMultiValueMetas(meta: Map<MetaKey, String>, delimiter: String): List<Pair<MetaKey, String>> {
    val result = mutableListOf<Pair<MetaKey, String>>()
    meta.forEach {
        val key = it.key
        val values = it.value.split(delimiter) //TODO: process delimiter escaping
        values.forEach { v ->
            result.add(Pair(key, v))
        }
    }
    return result
}