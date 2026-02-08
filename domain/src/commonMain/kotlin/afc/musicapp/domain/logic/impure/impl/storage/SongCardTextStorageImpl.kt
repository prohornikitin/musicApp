package afc.musicapp.domain.logic.impure.impl.storage

import afc.musicapp.domain.entities.SongCardText
import afc.musicapp.domain.entities.SongId
import afc.musicapp.domain.logic.impure.iface.MemoryCache
import afc.musicapp.domain.logic.impure.iface.db.DbQueryInterpreter
import afc.musicapp.domain.logic.impure.iface.storage.read.SongCardTextRead
import afc.musicapp.domain.logic.pure.sql.mainDb.MainDbSetup
import io.ktor.util.collections.ConcurrentMap
import kotlin.collections.putAll

class SongCardTextStorageImpl(
    private val db: DbQueryInterpreter,
) : SongCardTextRead {
    private val cache: ConcurrentMap<SongId, SongCardText> = ConcurrentMap()

    init {
        MemoryCache.Companion.add {
            cache.clear()
        }
    }

    override suspend fun get(ids: List<SongId>): Map<SongId, SongCardText> {
        val idsToLoad = ids.filter { !cache.containsKey(it) }
        if(idsToLoad.isNotEmpty()) {
            cache.putAll(
                db.executeOrDefault(MainDbSetup.getSongCardTextFor(idsToLoad))
            )
        }
        return cache
    }

    override suspend fun update(id: SongId, text: SongCardText) {
        cache[id] = text
        db.executeOrDefault(
            MainDbSetup.updateGeneratedTemplates(mapOf(
                id to text
            ))
        )
    }

    override suspend fun updateAll(newText: Map<SongId, SongCardText>) {
        cache.putAll(newText)
        db.executeOrDefault(
            MainDbSetup.updateGeneratedTemplates(newText)
        )
    }
}