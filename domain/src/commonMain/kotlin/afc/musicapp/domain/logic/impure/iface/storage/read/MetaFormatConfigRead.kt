package afc.musicapp.domain.logic.impure.iface.storage.read

interface MetaFormatConfigRead {
    suspend fun getDelimiter(): String
}