package afc.musicapp.domain.entities.codec.config

import afc.musicapp.domain.logic.pure.Optional
import kotlinx.io.files.Path

object OptionalPathCodec : ConfigCodec<Optional<Path>> {
    override fun encode(value: Optional<Path>): String {
        return value.getOrNull()?.toString() ?: ""
    }

    override fun decode(value: String): Optional<Path>? {
        return if(value.isEmpty()) {
            Optional.some(Path(value))
        } else {
            Optional.none()
        }
    }
}