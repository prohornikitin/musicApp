package afc.musicapp.domain.entities.codec.config

import afc.musicapp.domain.entities.SongCardTemplates
import afc.musicapp.domain.logic.pure.parseTemplate
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json

object TemplatesCodec : ConfigCodec<SongCardTemplates> {

    @Serializable
    private data class JsonObj(
        val main: String,
        val bottom: String,
    )
    override fun encode(value: SongCardTemplates) = Json.encodeToString(JsonObj(
        main = value.main.toString(),
        bottom = value.bottom.toString(),
    ))
    override fun decode(value: String): SongCardTemplates? {
        val fromJson = Json.decodeFromString<JsonObj>(value)
        return SongCardTemplates(
            parseTemplate(fromJson.main),
            parseTemplate(fromJson.bottom),
        )
    }
}