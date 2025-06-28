package com.example.musicapp.logic.pure.logic.sql

import org.junit.Test

import com.example.musicapp.domain.logic.pure.sql.Basic.and
import com.example.musicapp.domain.logic.pure.sql.Basic.andMany


class BasicAndTest {
    @Test
    fun and_common() {
        assertCharSeqEquals("(A AND B)", ("A" and "B"))
    }

    @Test
    fun and_leftEmpty() {
        assertCharSeqEquals("B", ("" and "B"))
    }

    @Test
    fun and_rightEmpty() {
        assertCharSeqEquals("A", ("A" and ""))
    }

    @Test
    fun and_bothEmpty() {
        assertCharSeqEquals("", ("" and ""))
    }

    @Test
    fun andMany_common() {
        assertCharSeqEquals("(A AND B AND C)", andMany(listOf(
            "A",
            "B",
            "C",
        )))
    }

    @Test
    fun andMany_firstIsEmpty() {
        assertCharSeqEquals("(B AND C)", andMany(listOf(
            "",
            "B",
            "C",
        )))
    }

    @Test
    fun andMany_midIsEmpty() {
        assertCharSeqEquals("(A AND C)", andMany(listOf(
            "A",
            "",
            "C",
        )))
    }

    @Test
    fun andMany_lastIsEmpty() {
        assertCharSeqEquals("(A AND B)", andMany(listOf(
            "A",
            "B",
            "",
        )))
    }

    @Test
    fun andMany_allAreEmpty() {
        assertCharSeqEquals("", andMany(listOf(
            "",
            "",
            "",
        )))
    }

    @Test
    fun andMany_noConditions() {
        assertCharSeqEquals("", andMany(emptyList()))
    }

    @Test
    fun andMany_singleCondition() {
        assertCharSeqEquals("A", andMany(listOf("A")))
    }
}