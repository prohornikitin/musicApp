package com.example.config.serializer

import com.example.config.SerializableEnum
import kotlinx.serialization.KSerializer
import kotlinx.serialization.descriptors.PrimitiveKind
import kotlinx.serialization.descriptors.PrimitiveSerialDescriptor
import kotlinx.serialization.descriptors.SerialDescriptor
import kotlinx.serialization.encoding.Decoder
import kotlinx.serialization.encoding.Encoder
import kotlin.enums.EnumEntries

open class EnumSerializer<T>(val values: EnumEntries<T>, val default: T) : KSerializer<T>
    where T : SerializableEnum, T : Enum<T>
{
    override val descriptor: SerialDescriptor = PrimitiveSerialDescriptor("FontFamily", PrimitiveKind.STRING)
    override fun deserialize(decoder: Decoder): T {
        return values.find {
            it.value == decoder.decodeString()
        } ?: default
    }

    override fun serialize(encoder: Encoder, value: T) {
        encoder.encodeString(value.value)
    }
}