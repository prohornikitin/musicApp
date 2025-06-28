package com.example.musicapp.ui.nav

import com.example.musicapp.R
import kotlinx.serialization.Serializable

sealed interface Route {
    @Serializable
    object Main : Route

    @Serializable
    object Settings : Route {
        @Serializable
        object Templates : Route

        @Serializable
        object SongCardStyle : Route
    }
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
}
