package afc.musicapp.uistate

import android.graphics.Bitmap
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import java.io.ByteArrayOutputStream
import kotlin.reflect.KProperty


operator fun <T> StateFlow<T>.getValue(thisObj: Any?, property: KProperty<*>): T = value

operator fun <T> MutableStateFlow<T>.setValue(thisObj: Any?, property: KProperty<*>, value: T) {
    this.value = value
}

fun Bitmap.toByteArray(): ByteArray {
    val stream = ByteArrayOutputStream()
    compress(Bitmap.CompressFormat.PNG, 100, stream)
    val byteArray = stream.toByteArray()
    recycle()
    return byteArray
}