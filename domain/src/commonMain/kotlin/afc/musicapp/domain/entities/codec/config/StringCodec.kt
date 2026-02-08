package afc.musicapp.domain.entities.codec.config


object StringCodec : ConfigCodec<String> {
    override fun encode(value: String) = value.toString()
    override fun decode(value: String): String = value
}