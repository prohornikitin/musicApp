package afc.musicapp.domain.entities.codec.config

interface ConfigCodec<T : Any> {
    fun encode(value: T): String
    fun decode(value: String): T?
}