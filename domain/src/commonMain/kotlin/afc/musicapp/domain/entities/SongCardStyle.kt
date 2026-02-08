package afc.musicapp.domain.entities

import afc.musicapp.domain.entities.codec.FontFamilySerializer
import afc.musicapp.domain.entities.codec.FontStyleSerializer
import afc.musicapp.domain.entities.codec.IconVisibilitySerializer
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
