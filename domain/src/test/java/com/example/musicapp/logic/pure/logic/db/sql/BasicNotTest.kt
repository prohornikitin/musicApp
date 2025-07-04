package com.example.musicapp.logic.pure.logic.db.sql

import com.example.musicapp.domain.logic.pure.sql.Basic.not
import com.example.musicapp.logic.pure.logic.assertCharSeqEquals
import org.junit.Test


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