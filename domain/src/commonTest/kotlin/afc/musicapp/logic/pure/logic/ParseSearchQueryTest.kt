package com.example.musicapp.logic.pure.logic

import afc.musicapp.domain.entities.MetaKey
import afc.musicapp.domain.entities.SearchQuery
import afc.musicapp.domain.logic.pure.parseSearchQuery
import kotlin.test.Test
import kotlin.test.assertTrue
import kotlin.test.assertEquals

class ParseSearchQueryTest {
    @Test
    fun empty() {
        assertTrue(parseSearchQuery("").isEmpty())
    }

    @Test
    fun plainOnly() {
        assertEquals(
            SearchQuery(
                emptyMap(),
                "plain",
            ),
            parseSearchQuery("plain"),
        )
    }

    @Test
    fun simpleMetasOnly() {
        assertEquals(
            SearchQuery(
                mapOf(
                    "keyOne" to "value1",
                    "keyTwo" to "value2",
                ).mapKeys{ MetaKey(it.key) },
                "",
            ),
            parseSearchQuery("keyOne:value1  keyTwo:value2"),
        )
    }

    @Test
    fun metasInQuotes() {
        assertEquals(
            SearchQuery(
                mapOf(
                    "key" to "value",
                ).mapKeys{ MetaKey(it.key) },
                "",
            ),
            parseSearchQuery("key:\"value\""),
        )
    }
}