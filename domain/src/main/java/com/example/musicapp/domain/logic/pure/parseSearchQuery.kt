package com.example.musicapp.domain.logic.pure

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SearchQuery

fun parseSearchQuery(query: String) : SearchQuery {
    val metaRegex = "([a-zA-Z]+):(([^\\s']+)|(\"[^\"']+\"))".toRegex()
    val plainQuery = query.replace(metaRegex, "")
    val metas = metaRegex.findAll(query).map {
        val (k,v) = it.value.split(":")
        val value = if(v.startsWith('"') && v.endsWith('"')) {
            v.substring(1, v.length-1)
        } else v
        val key = MetaKey(k)
        Pair(key, value)
    }.toMap()
    return SearchQuery(
        metaPrefixes = metas,
        plain = plainQuery.replace("  ", " ").trim(),
    )
}
