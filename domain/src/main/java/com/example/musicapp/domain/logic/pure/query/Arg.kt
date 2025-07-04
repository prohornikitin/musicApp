package com.example.musicapp.domain.logic.pure.query

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