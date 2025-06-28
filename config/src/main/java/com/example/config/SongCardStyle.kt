package com.example.config

import com.example.config.serializer.FontFamilySerializer
import com.example.config.serializer.FontStyleSerializer
import com.example.config.serializer.IconVisibilitySerializer
import kotlinx.serialization.Serializable


@Serializable
data class SongCardStyle(
    val iconSizeDp: Int = 40,
    val iconVisibility: IconVisibility = IconVisibility.Show,
    val mainFont: FontConfig = FontConfig(),
    val bottomFont: FontConfig = FontConfig(color = RgbaColor.Gray),
) {
    @Serializable
    data class FontConfig(
        @Serializable(with = FontFamilySerializer::class)
        val family: FontFamily = FontFamily.Default,

        @Serializable(with = FontStyleSerializer::class)
        val style: FontStyle = FontStyle.Normal,

        val weight: Int = 400,

        val sizeSp: Int = 12,

        val color: RgbaColor = RgbaColor.Black,
    )

    @Serializable(with = IconVisibilitySerializer::class)
    enum class IconVisibility(override val value: String) : SerializableEnum {
        Show("Show"),
        ShowIfAvailable("ShowIfAvailable"),
        Hide("Hide"),
        ;
    }
}
