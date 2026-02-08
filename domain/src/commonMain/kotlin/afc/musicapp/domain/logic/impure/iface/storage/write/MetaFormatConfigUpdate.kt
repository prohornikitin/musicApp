package afc.musicapp.domain.logic.impure.iface.storage.write

interface MetaFormatConfigUpdate {
    suspend fun setDelimiter(delimiter: String)
}