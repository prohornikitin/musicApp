package afc.musicapp.domain.logic.pure

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SearchQuery

fun parseSearchQuery(query: String) : SearchQuery {
    val metaRegex = "([a-zA-Z]+):(([^\\s']+)|(\"[^\"']+\"))".toRegex()
    val plainQuery = query.replace(metaRegex, "").lowercase()
    val metas = metaRegex.findAll(query).map {
        val (k,v) = it.value.split(":")
        val value = if(v.startsWith('"') && v.endsWith('"')) {
            v.substring(1, v.length-1).lowercase()
        } else v.lowercase()
        val key = MetaKey(k)
        Pair(key, value)
    }.toMap()
    return SearchQuery(
        metaPrefixes = metas,
        plain = plainQuery.replace("  ", " ").trim(),
    )
}
