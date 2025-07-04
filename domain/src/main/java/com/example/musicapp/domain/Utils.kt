package com.example.musicapp.domain

import com.example.config.RgbaColor

fun StringBuilder.surroundedBy(surrounded: String, block: StringBuilder.() -> Unit) {
    append(surrounded)
    block()
    append(surrounded)
}

fun <T> Collection<T>.joinToSb(
    builder: StringBuilder,
    separator: String,
    addItem: StringBuilder.(T) -> Unit
) {
    this.forEachIndexed { index, item ->
        builder.addItem(item)
        if(index != size-1) {
            builder.append(separator)
        }
    }
}


fun <T> Array<T>.joinToSb(
    builder: StringBuilder,
    separator: String,
    addItem: StringBuilder.(T) -> Unit
) {
    this.forEachIndexed { index, item ->
        builder.addItem(item)
        if(index != size-1) {
            builder.append(separator)
        }
    }
}

fun String.fromHexIntOrNull(): Long? {
    var number = 0L
    forEach {
        val hexDigit = if (it.isDigit()) {
            it.digitToInt()
        } else when(it) {
            'A', 'a' -> 10
            'B', 'b' -> 11
            'C', 'c' -> 12
            'D', 'd' -> 13
            'E', 'e' -> 14
            'F', 'f' -> 15
            else -> return null
        }
        number = number * 16 + hexDigit
    }
    return number
}

@OptIn(ExperimentalStdlibApi::class)
fun Long.toHexStr(): String {
    return toHexString(HexFormat {
        upperCase = true
        number {
            removeLeadingZeros = true
            minLength = 6
        }
    })
}

fun String.toColorOrNull(): RgbaColor? {
    if((!startsWith('#'))) {
        return null
    }
    if(length == 7) {
        substring(1).fromHexIntOrNull()?.let {
            return RgbaColor(0xFF000000 + it)
        }
    }
    if(length == 9) {
        substring(1).fromHexIntOrNull()?.let {
            return RgbaColor(it)
        }
    }
    return null
}

fun String.escapeSqlString(): String = replace("'", "''")

fun <K, V> Map<K, V>?.toNotNullMap() = this ?: emptyMap()