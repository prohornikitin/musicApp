package afc.musicapp.data.pure.sql

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

    inline fun <T> StringBuilder.inClause(
        iterable: Collection<T>,
        crossinline f: StringBuilder.(T) -> Unit = { append(it) },
    ) {
        append(" IN (")
        iterable.joinToSb(this, ",") {
            f(it)
        }
        append(")")
    }



    fun <T> Collection<T>.joinToSb(
        builder: StringBuilder,
        separator: String,
        addItem: StringBuilder.(T) -> Unit
    ) {
        this.forEachIndexed { index, item ->
            builder.addItem(item)
            if(index != size-1) {
                builder.append(separator)
            }
        }
    }

}