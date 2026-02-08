package afc.musicapp.ui

import androidx.compose.foundation.layout.systemBarsPadding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import androidx.navigation.toRoute
import afc.musicapp.domain.entities.SongId
import afc.musicapp.ui.complex.MainScreen
import afc.musicapp.ui.complex.SongCardStyleEditor
import afc.musicapp.ui.complex.TemplateEditor
import afc.musicapp.ui.complex.tagEditor.TagEditorScreen
import afc.musicapp.ui.nav.Route
import afc.musicapp.ui.nav.topLevelRoutes
import afc.musicapp.ui.nav.getTitleRes
import afc.musicapp.ui.theme.MusicAppTheme
import afc.musicapp.uistate.config.SongCardStyleEditorVm
import afc.musicapp.uistate.config.TemplateEditorVm
import afc.musicapp.uistate.vm.MainVm
import afc.musicapp.uistate.vm.PlayerVm
import afc.musicapp.uistate.vm.TagEditorVm
import kotlinx.coroutines.launch
import org.jetbrains.compose.resources.stringResource
import org.kodein.di.DI
import org.kodein.di.compose.viewmodel.rememberViewModel
import org.kodein.di.compose.withDI

@Composable
fun App(di: DI) = withDI(di) {
    val vm by rememberViewModel<MainVm>()
    val templateEditorVm by rememberViewModel<TemplateEditorVm>()
    val songCardStyleEditorVm by rememberViewModel<SongCardStyleEditorVm>()
    val playerVm by rememberViewModel<PlayerVm>()
    val tagEditorVm by rememberViewModel<TagEditorVm>()

    val navController = rememberNavController()
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val openDrawer: () -> Unit = {
        scope.launch { drawerState.open() }
    }
    MusicAppTheme {
        ModalNavigationDrawer(
            drawerState = drawerState,
            drawerContent = {
                ModalDrawerSheet {
                    val navBackStackEntry by navController.currentBackStackEntryAsState()
                    val currentDestination = navBackStackEntry?.destination
                    topLevelRoutes.forEach { topLevelRoute ->
                        NavigationDrawerItem(
                            label = {
                                Text(
                                    text = stringResource(topLevelRoute.route.getTitleRes()),
                                    fontWeight = FontWeight.Bold
                                )
                            },
                            selected = topLevelRoute.children.isEmpty() && currentDestination?.hierarchy?.any {
                                it.hasRoute(topLevelRoute.route::class)
                            } == true,
                            onClick = {
                                scope.launch { drawerState.close() }
                                navController.navigate(topLevelRoute.route) {
                                    popUpTo(navController.graph.findStartDestination().id) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            },
                            modifier = Modifier,
                        )
                        topLevelRoute.children.forEach { route ->
                            NavigationDrawerItem(
                                label = { Text(text = stringResource(route.getTitleRes())) },
                                selected = currentDestination?.hierarchy?.any {
                                    it.hasRoute(
                                        route::class
                                    )
                                } == true,
                                onClick = {
                                    scope.launch { drawerState.close() }
                                    navController.navigate(route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                },
                            )
                        }
                        HorizontalDivider()
                    }
                }
            }
        ) {
            Scaffold { padding ->
                NavHost(
                    navController,
                    startDestination = Route.Main,
                    modifier = Modifier.systemBarsPadding()
                ) {
                    composable<Route.Main> {
                        MainScreen(vm, playerVm, openDrawer, navController)
                    }
                    composable<Route.TagEditor> { backStackEntry ->
                        val route = backStackEntry.toRoute<Route.TagEditor>()
                        tagEditorVm.song = SongId(route.id)
                        TagEditorScreen(tagEditorVm, navController)
                    }
                    navigation<Route.Settings>(startDestination = Route.Settings.SongCardStyle) {
                        composable<Route.Settings.SongCardStyle> {
                            SongCardStyleEditor(songCardStyleEditorVm, openDrawer)
                        }

                        composable<Route.Settings.Templates> {
                            TemplateEditor(templateEditorVm, openDrawer)
                        }
                    }
                }
            }
        }
    }
}