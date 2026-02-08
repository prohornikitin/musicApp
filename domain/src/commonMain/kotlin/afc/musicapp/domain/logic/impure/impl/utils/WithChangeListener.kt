package afc.musicapp.domain.logic.impure.impl.utils

import kotlin.reflect.KProperty


class WithChangeListener <T> (initialValue: T, val listener: (T) -> Unit) {
    private var field: T = initialValue

    operator fun getValue(thisRef: Any?, property: KProperty<*>): T = field
    operator fun setValue(thisRef: Any?, property: KProperty<*>, value: T) {
        if(field == value) {
            return
        }
        field = value
        listener(value)
    }
}