package afc.musicapp.domain.logic.impure.iface.storage

interface ConfigStorage {
    suspend fun get(key: String): String?
    suspend fun set(key: String, value: String)
}