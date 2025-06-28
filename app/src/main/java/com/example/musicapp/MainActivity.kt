package com.example.musicapp

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.os.Environment
import android.provider.Settings
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import androidx.navigation.NavDestination.Companion.hasRoute
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.navigation
import androidx.navigation.compose.rememberNavController
import com.example.musicapp.ui.complex.MainScreen
import com.example.musicapp.ui.complex.SongCardStyleEditor
import com.example.musicapp.ui.complex.TemplateEditor
import com.example.musicapp.ui.nav.Route
import com.example.musicapp.ui.nav.getTitleRes
import com.example.musicapp.ui.nav.topLevelRoutes
import com.example.musicapp.ui.theme.MusicAppTheme
import com.example.musicapp.uistate.MainVm
import com.example.musicapp.uistate.PlayerVm
import com.example.musicapp.uistate.config.SongCardStyleEditorVm
import com.example.musicapp.uistate.config.TemplateEditorVm
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.launch
import java.lang.String.format

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    private val vm by viewModels<MainVm>()
    private val templateEditorVm by viewModels<TemplateEditorVm>()
    private val songCardStyleEditorVm by viewModels<SongCardStyleEditorVm>()
    private val playerVm by viewModels<PlayerVm>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
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
                                    label = { Text(
                                        text = stringResource(topLevelRoute.route.getTitleRes()),
                                        fontWeight = FontWeight.Bold
                                    ) },
                                    selected = topLevelRoute.children.isEmpty() && currentDestination?.hierarchy?.any { it.hasRoute(topLevelRoute.route::class) } == true,
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
                                        selected = currentDestination?.hierarchy?.any { it.hasRoute(route::class) } == true,
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
                    NavHost(navController, startDestination = Route.Main) {
                        composable<Route.Main> {
                            MainScreen(vm, playerVm, openDrawer)
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

    override fun onResume() {
        super.onResume()
        vm.onResume()
    }

    private fun hasPermissions(context: Context): Boolean {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            Environment.isExternalStorageManager()
        } else {
            ContextCompat.checkSelfPermission(
                context,
                android.Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions(activity: Activity, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.setData(format("package:%s", activity.packageName).toUri())
                activity.startActivityForResult(intent, requestCode)
            } catch (_: Exception) {
                val intent = Intent()
                intent.setAction(Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION)
                activity.startActivityForResult(intent, requestCode)
            }

        } else {
//            ActivityCompat.requestPermissions(
//                activity,
//                arrayOf<String>(Manifest.permission.WRITE_EXTERNAL_STORAGE),
//                requestCode
//            )
        }
    }

    override fun onStart() {
        super.onStart()
        if(!hasPermissions(applicationContext)) {
            requestPermissions(this, 1)
        }
    }
}





