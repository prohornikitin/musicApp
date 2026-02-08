package com.example.musicapp.logic.pure.logic.db.sql

import afc.musicapp.domain.logic.pure.sql.Basic.or
import afc.musicapp.domain.logic.pure.sql.Basic.orMany
import com.example.musicapp.logic.pure.logic.assertCharSeqEquals
import kotlin.test.Test


class BasicOrTest {
    @Test
    fun or_common() {
        assertCharSeqEquals("(A) OR (B)", ("A" or "B"))
    }

    @Test
    fun or_leftEmpty() {
        assertCharSeqEquals("B", ("" or "B"))
    }

    @Test
    fun or_rightEmpty() {
        assertCharSeqEquals("A", ("A" or ""))
    }

    @Test
    fun or_bothEmpty() {
        assertCharSeqEquals("", ("" or ""))
    }

    @Test
    fun orMany_common() {
        assertCharSeqEquals(
            "(A) OR (B) OR (C)", orMany(
                listOf(
                    "A",
                    "B",
                    "C",
                )
            )
        )
    }

    @Test
    fun orMany_firstIsEmpty() {
        assertCharSeqEquals(
            "(B) OR (C)", orMany(
                listOf(
                    "",
                    "B",
                    "C",
                )
            )
        )
    }

    @Test
    fun orMany_midIsEmpty() {
        assertCharSeqEquals(
            "(A) OR (C)", orMany(
                listOf(
                    "A",
                    "",
                    "C",
                )
            )
        )
    }

    @Test
    fun orMany_lastIsEmpty() {
        assertCharSeqEquals(
            "(A) OR (B)", orMany(
                listOf(
                    "A",
                    "B",
                    "",
                )
            )
        )
    }

    @Test
    fun orMany_allAreEmpty() {
        assertCharSeqEquals(
            "", orMany(
                listOf(
                    "",
                    "",
                    "",
                )
            )
        )
    }

    @Test
    fun orMany_noConditions() {
        assertCharSeqEquals("", orMany(emptyList()))
    }

    @Test
    fun orMany_singleCondition() {
        assertCharSeqEquals("A", orMany(listOf("A")))
    }
}