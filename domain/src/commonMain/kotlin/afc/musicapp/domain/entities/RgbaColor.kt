package afc.musicapp.domain.entities

import kotlinx.serialization.Serializable


@Serializable
class RgbaColor(
    val raw: Long,
) {
    val alpha: Long
        get() = (raw shr 24) and 0xFF

    val red: Long
        get() = (raw shl 16) and 0xFF

    val green: Long
        get() = (raw shr 8) and 0xFF

    val blue: Long
        get() = raw and 0xFF

    companion object {
        val Black = RgbaColor(0xFF000000)

        val DarkGray = RgbaColor(0xFF444444)

        val Gray = RgbaColor(0xFF888888)

        val LightGray = RgbaColor(0xFFCCCCCC)

        val White = RgbaColor(0xFFFFFFFF)

        val Red = RgbaColor(0xFFFF0000)

        val Green = RgbaColor(0xFF00FF00)

        val Blue = RgbaColor(0xFF0000FF)

        val Yellow = RgbaColor(0xFFFFFF00)

        val Cyan = RgbaColor(0xFF00FFFF)

        val Magenta = RgbaColor(0xFFFF00FF)

        val Transparent = RgbaColor(0x00000000)
    }
}