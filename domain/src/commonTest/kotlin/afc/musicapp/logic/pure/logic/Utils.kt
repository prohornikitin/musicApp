package com.example.musicapp.logic.pure.logic

import kotlin.test.assertEquals

fun assertCharSeqEquals(expected: CharSequence, actual: CharSequence) {
    assertEquals(expected.toString(), actual.toString())
}