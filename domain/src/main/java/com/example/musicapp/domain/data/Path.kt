package com.example.musicapp.domain.data

class Path private constructor(
    arg: String
) {
    private val str: String = when {
        arg.endsWith(separator) -> arg.dropLast(separator.length)
        else -> arg
    }


    companion object {
        val separator: String = "/"
    }

    infix operator fun div(other: String): Path = Path(buildString {
        append(str)
        if (!other.startsWith(separator)) {
            append(separator)
        }
        if(!other.endsWith(separator)) {
            append(other)
        } else {
            append(other.dropLast(separator.length))
        }
    })

    infix operator fun div(other: Path): Path = Path(buildString {
        append(str)
        append(separator)
        append(other.str)
    })
}