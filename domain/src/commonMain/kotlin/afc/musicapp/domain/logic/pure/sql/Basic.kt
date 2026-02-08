package afc.musicapp.domain.logic.pure.sql

import kotlin.text.isEmpty

object Basic {
    private fun binaryOp(op: CharSequence, a: CharSequence, b: CharSequence): CharSequence {
        if (a.isEmpty()) {
            return b
        }
        if (b.isEmpty()) {
            return a
        }
        return StringBuilder().apply {
            append('(')
            append(a)
            append(") ")
            append(op)
            append(" (")
            append(b)
            append(')')
        }
    }

    private fun binaryOpMany(op: CharSequence, conditions: Collection<CharSequence>): CharSequence {
        val nonEmptyConditions = conditions.filter { it.isNotEmpty() }
        if (nonEmptyConditions.isEmpty()) {
            return ""
        }
        if (nonEmptyConditions.size == 1) {
            return nonEmptyConditions.first()
        }
        return StringBuilder().apply {
            nonEmptyConditions.forEachIndexed { index, item ->
                if (index > 0) {
                    append(' ')
                    append(op)
                    append(' ')
                }
                append('(')
                append(item)
                append(')')
            }
        }
    }

    infix fun CharSequence.or(other: CharSequence): CharSequence {
        return binaryOp("OR", this, other)
    }

    fun orMany(conditions: Collection<CharSequence>): CharSequence{
        return binaryOpMany("OR", conditions)
    }

    infix fun CharSequence.and(other: CharSequence): CharSequence {
        return binaryOp("AND", this, other)
    }

    fun andMany(conditions: Collection<CharSequence>): CharSequence {
        return binaryOpMany("AND", conditions)
    }

    fun not(a: CharSequence): CharSequence {
        if (a.isEmpty()) {
            return a
        }
        return StringBuilder().apply {
            append("(NOT ")
            append(a)
            append(')')
        }
    }

    fun transaction(vararg queries: CharSequence): String {
        return buildString {
            append("BEGIN; ")
            for(query in queries) {
                append(query)
                append(";")
            }
            append("COMMIT; ")
        }
    }
}