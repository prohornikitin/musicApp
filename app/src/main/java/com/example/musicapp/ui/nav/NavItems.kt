package com.example.musicapp.ui.nav

import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Parcelable
import androidx.lifecycle.SavedStateHandle
import androidx.navigation.NavType
import androidx.navigation.toRoute
import com.example.musicapp.R
import com.example.musicapp.domain.data.SongId
import kotlinx.parcelize.RawValue
import kotlinx.serialization.Contextual
import kotlinx.serialization.Serializable
import kotlinx.serialization.json.Json
import kotlin.reflect.typeOf

sealed interface Route : java.io.Serializable{
    @Serializable
    object Main : Route

    @Serializable
    data class TagEditor(val id: Long) : Route {
        companion object {
            val typeMap = mapOf(typeOf<TagEditor>() to NavType.SerializableType(TagEditor::class.java))

            fun from(savedStateHandle: SavedStateHandle) =
                savedStateHandle.toRoute<TagEditor>(typeMap)
        }
    }

    @Serializable
    object Settings : Route {
        @Serializable
        object Templates : Route

        @Serializable
        object SongCardStyle : Route
    }
}

inline fun <reified T : Parcelable> parcelableType(
    isNullableAllowed: Boolean = false,
    json: Json = Json,
) = object : NavType<T>(isNullableAllowed = isNullableAllowed) {
    override fun get(bundle: Bundle, key: String) =
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
            bundle.getParcelable(key, T::class.java)
        } else {
            @Suppress("DEPRECATION")
            bundle.getParcelable(key)
        }

    override fun parseValue(value: String): T = json.decodeFromString(value)
    override fun serializeAsValue(value: T): String = Uri.encode(json.encodeToString(value))
    override fun put(bundle: Bundle, key: String, value: T) = bundle.putParcelable(key, value)
}

data class TopLevelRoute<T : Route>(val route: T, val children: List<Route>)

val topLevelRoutes = listOf(
    TopLevelRoute(Route.Main, emptyList()),
    TopLevelRoute(Route.Settings, listOf(
        Route.Settings.Templates,
        Route.Settings.SongCardStyle,
    )),
)


fun Route.getTitleRes() = when(this) {
    Route.Main -> R.string.main
    Route.Settings -> R.string.settings
    Route.Settings.SongCardStyle -> R.string.settings_card_style
    Route.Settings.Templates -> R.string.settings_template
    is Route.TagEditor -> R.string.tag_editor
}
