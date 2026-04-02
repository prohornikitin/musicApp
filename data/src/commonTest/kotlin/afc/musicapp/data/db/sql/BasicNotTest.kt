package com.example.musicapp.logic.pure.logic.db.sql

import afc.musicapp.data.pure.sql.Basic.not
import afc.musicapp.data.db.assertCharSeqEquals
import kotlin.test.Test

class BasicNotTest {
    @Test
    fun not_common() {
        assertCharSeqEquals("(NOT A)", not("A"))
    }

    @Test
    fun not_emptyCondition() {
        assertCharSeqEquals("", not(""))
    }
}