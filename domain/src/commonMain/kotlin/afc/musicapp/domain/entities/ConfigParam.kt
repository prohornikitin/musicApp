package afc.musicapp.domain.entities

import afc.musicapp.domain.entities.codec.config.ConfigCodec
import afc.musicapp.domain.entities.codec.config.OptionalPathCodec
import afc.musicapp.domain.entities.codec.config.StringCodec
import afc.musicapp.domain.entities.codec.config.TemplatesCodec
import afc.musicapp.domain.logic.pure.Optional
import afc.musicapp.domain.logic.pure.parseTemplate
import okio.Path


class ConfigParam<T: Any> internal constructor(
    val key: String,
    val default: T,
    val codec: ConfigCodec<T>,
) {
    companion object {
        internal inline fun <reified T: Any> create(key: String, default: T, codec: ConfigCodec<T>): ConfigParam<T> {
            return ConfigParam(key, default, codec)
        }

        val templates =  create(
            "templates",
            SongCardTemplates(
                parseTemplate("#TITLE"),
                parseTemplate("#ARTIST"),
            ),
            TemplatesCodec
        )
//        val mainTemplate =  create("main_template", parseTemplate("#TITLE"), TemplateCodec)
//        val subTemplate =   create("sub_template", parseTemplate("#ARTIST"), TemplateCodec)
        val metaDelimiter = create("meta_delimiter", "/", StringCodec)
        @Suppress("unchecked")
        val musicFolders =  create<Optional<Path>>("music_folder", Optional.Companion.none(), OptionalPathCodec as ConfigCodec<Optional<Path>>) //TODO: platform-specific default, how?
    }
}