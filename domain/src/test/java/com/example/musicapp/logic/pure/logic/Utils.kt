package com.example.musicapp.logic.pure.logic

import junit.framework.TestCase.assertEquals


fun assertCharSeqEquals(expected: CharSequence, actual: CharSequence) {
    assertEquals(expected.toString(), actual.toString())
}