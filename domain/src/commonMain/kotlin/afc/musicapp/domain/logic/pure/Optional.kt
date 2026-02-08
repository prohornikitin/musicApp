package afc.musicapp.domain.logic.pure

class Optional<out T : Any> private constructor(
    private val value: T?
){
    fun isNone() = value == null
    fun haveSomething() = value != null
    fun getOrNull() = value

    fun <K: Any> map(f: (T) -> K?): Optional<K> {
        if (value == null) {
            return none()
        }
        val new = f(value)
        return if (new == null) {
            none()
        } else {
            some(new)
        }
    }

    fun <K: Any> flatMap(f: (T) -> Optional<K>) {
        return
    }

    companion object {
        fun <T : Any> none() = Optional<T>(null)
        fun <T : Any> some(value: T) = Optional<T>(value)
        fun <T : Any> fromNullable(value: T?) = Optional(value)
    }
}