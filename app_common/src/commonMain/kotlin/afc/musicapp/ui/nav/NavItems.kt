package afc.musicapp.ui.nav

import kotlinx.serialization.Serializable
import musicapp.app_common.generated.resources.Res
import musicapp.app_common.generated.resources.main
import musicapp.app_common.generated.resources.settings
import musicapp.app_common.generated.resources.settings_card_style
import musicapp.app_common.generated.resources.settings_template
import musicapp.app_common.generated.resources.tag_editor
import org.jetbrains.compose.resources.StringResource

sealed interface Route {
    @Serializable
    object Main : Route

    @Serializable
    data class TagEditor(val id: Long) : Route

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


fun Route.getTitleRes(): StringResource = when(this) {
    Route.Main -> Res.string.main
    Route.Settings -> Res.string.settings
    Route.Settings.SongCardStyle -> Res.string.settings_card_style
    Route.Settings.Templates -> Res.string.settings_template
    is Route.TagEditor -> Res.string.tag_editor
}
