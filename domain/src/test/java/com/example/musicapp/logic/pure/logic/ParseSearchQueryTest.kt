package com.example.musicapp.logic.pure.logic

import com.example.musicapp.domain.data.MetaKey
import com.example.musicapp.domain.data.SearchQuery
import com.example.musicapp.domain.logic.pure.parseSearchQuery
import junit.framework.TestCase.assertEquals
import org.junit.Test

class ParseSearchQueryTest {
    @Test
    fun empty() {
        assert(parseSearchQuery("").isEmpty())
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