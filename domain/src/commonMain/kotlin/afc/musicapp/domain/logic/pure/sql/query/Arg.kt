package afc.musicapp.domain.logic.pure.sql.query

import kotlin.jvm.JvmInline

sealed interface Arg {
    companion object {
        fun of(string: String?) = StringArg(string)
        fun of(blob: ByteArray?) = BlobArg(blob)
        fun of(num: Long?) = LongArg(num)
    }
}

@JvmInline
value class StringArg(val data: String?) : Arg

@JvmInline
value class BlobArg(val data: ByteArray?) : Arg

@JvmInline
value class LongArg(val data: Long?) : Arg