package com.example.musicapp.domain.data

data class Template(
    val entries: List<Entry> = emptyList()
) {
    sealed interface Entry {
        val sourceIndexes: IntRange
    }
    data class PlainText(
        val text: String,
        override val sourceIndexes: IntRange,
    ) : Entry
    data class MetaField(
        val key: MetaKey,
        override val sourceIndexes: IntRange
    ) : Entry

    fun getUsedKeys(): Set<MetaKey> {
        return HashSet(entries.mapNotNull {
            when(it) {
                is MetaField -> it.key
                is PlainText -> null
            }
        })
    }

    fun isEmpty(): Boolean {
        return this.entries.any {
            when (it) {
                is MetaField -> false
                is PlainText -> it.text.isEmpty()
            }
        }
    }

    override fun toString(): String = buildString {
        entries.forEach {
            when(it) {
                is MetaField -> {
                    append('#')
                    append(it.key.raw)
                }
                is PlainText -> {
                    append(it.text)
                }
            }
        }
    }
}