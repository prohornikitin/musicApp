package afc.musicapp.di.context

import android.Manifest
import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.ComponentName
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.content.IntentSender
import android.content.ServiceConnection
import android.content.res.Configuration
import android.database.DatabaseErrorHandler
import android.database.sqlite.SQLiteDatabase
import android.graphics.Bitmap
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.os.Handler
import android.os.UserHandle
import android.view.Display
import androidx.annotation.RequiresApi
import androidx.annotation.RequiresPermission
import java.io.InputStream

abstract class ApplicationContext : Context() {
    companion object {
        @Suppress("DEPRECATION")
        fun wrap(source: Context): ApplicationContext {
            return object : ApplicationContext() {
                override fun bindService(p0: Intent, p1: ServiceConnection, p2: Int) = source.bindService(p0, p1, p2)
                override fun checkCallingOrSelfPermission(p0: String) = source.checkCallingOrSelfPermission(p0)
                override fun checkCallingOrSelfUriPermission(p0: Uri?, p1: Int) = source.checkCallingOrSelfUriPermission(p0, p1)
                override fun checkCallingPermission(p0: String) = source.checkCallingPermission(p0)
                override fun checkCallingUriPermission(p0: Uri?, p1: Int) = source.checkCallingUriPermission(p0, p1)
                override fun checkPermission(p0: String, p1: Int, p2: Int) = source.checkPermission(p0, p1, p2)
                override fun checkSelfPermission(p0: String) = source.checkSelfPermission(p0)
                override fun checkUriPermission(p0: Uri?, p1: Int, p2: Int, p3: Int) = source.checkUriPermission(p0, p1, p2, p3)
                override fun checkUriPermission(p0: Uri?, p1: String?, p2: String?, p3: Int, p4: Int, p5: Int) = source.checkUriPermission(p0, p1, p2, p3, p4, p5)
                override fun createConfigurationContext(p0: Configuration) = source.createConfigurationContext(p0)
                override fun createDeviceProtectedStorageContext() = source.createDeviceProtectedStorageContext()
                override fun createDisplayContext(p0: Display) = source.createDisplayContext(p0)
                override fun createPackageContext(p0: String?, p1: Int) = source.createPackageContext(p0, p1)
                override fun databaseList() = source.databaseList()
                override fun deleteDatabase(p0: String?) = source.deleteDatabase(p0)
                override fun deleteFile(p0: String?) = source.deleteFile(p0)
                override fun deleteSharedPreferences(p0: String?) = source.deleteSharedPreferences(p0)
                override fun enforceCallingOrSelfPermission(p0: String, p1: String?) = source.enforceCallingOrSelfPermission(p0, p1)
                override fun enforceCallingOrSelfUriPermission(p0: Uri?, p1: Int, p2: String?) = source.enforceCallingOrSelfUriPermission(p0, p1, p2)
                override fun enforceCallingPermission(p0: String, p1: String?) = source.enforceCallingPermission(p0, p1)
                override fun enforceCallingUriPermission(p0: Uri?, p1: Int, p2: String?) = source.enforceCallingUriPermission(p0, p1, p2)
                override fun enforcePermission(p0: String, p1: Int, p2: Int, p3: String?) = source.enforcePermission(p0, p1, p2, p3)
                override fun enforceUriPermission(p0: Uri?, p1: Int, p2: Int, p3: Int, p4: String?) = source.enforceUriPermission(p0, p1, p2, p3, p4)
                override fun enforceUriPermission(p0: Uri?, p1: String?, p2: String?, p3: Int, p4: Int, p5: Int, p6: String?) = source.enforceUriPermission(p0, p1, p2, p3, p4, p5, p6)
                override fun fileList() = source.fileList()
                override fun getApplicationContext() = source
                override fun getApplicationInfo() = source.applicationInfo
                override fun getAssets() = source.assets
                override fun getCacheDir() = source.cacheDir
                override fun getClassLoader() = source.classLoader
                override fun getCodeCacheDir() = source.codeCacheDir
                override fun getContentResolver() = source.contentResolver
                override fun getDataDir() = source.dataDir
                override fun getDatabasePath(p0: String?) = source.getDatabasePath(p0)
                override fun getDir(p0: String?, p1: Int) = source.getDir(p0, p1)
                override fun getExternalCacheDir() = source.externalCacheDir
                override fun getExternalCacheDirs() = source.externalCacheDirs
                override fun getExternalFilesDir(p0: String?) = source.getExternalFilesDir(p0)
                override fun getExternalFilesDirs(p0: String?) = source.getExternalFilesDirs(p0)
                override fun getExternalMediaDirs() = source.externalMediaDirs
                override fun clearWallpaper() = source.clearWallpaper()
                override fun getFileStreamPath(p0: String?) = source.getFileStreamPath(p0)
                override fun getFilesDir() = source.filesDir
                override fun getMainLooper() = source.mainLooper
                override fun getNoBackupFilesDir() = source.noBackupFilesDir
                override fun getObbDir() = source.obbDir
                override fun getObbDirs() = source.obbDirs
                override fun getPackageCodePath() = source.packageCodePath
                override fun getPackageManager() = source.packageManager
                override fun getPackageName() = source.packageName
                override fun getPackageResourcePath() = source.packageResourcePath
                override fun getResources() = source.resources
                override fun getSharedPreferences(p0: String?, p1: Int) = source.getSharedPreferences(p0, p1)
                override fun getSystemService(p0: String) = source.getSystemService(p0)
                override fun getSystemServiceName(p0: Class<*>) = source.getSystemServiceName(p0)
                override fun getTheme() = source.theme
                override fun getWallpaper() = source.wallpaper
                override fun getWallpaperDesiredMinimumHeight() = source.wallpaperDesiredMinimumHeight
                override fun getWallpaperDesiredMinimumWidth() = source.wallpaperDesiredMinimumHeight
                override fun grantUriPermission(p0: String?, p1: Uri?, p2: Int) = source.grantUriPermission(p0, p1, p2)
                override fun isDeviceProtectedStorage() = source.isDeviceProtectedStorage
                override fun moveDatabaseFrom(p0: Context?, p1: String?) = source.moveDatabaseFrom(p0, p1)
                override fun moveSharedPreferencesFrom(p0: Context?, p1: String?) = source.moveSharedPreferencesFrom(p0, p1)
                override fun openFileInput(p0: String?) = source.openFileInput(p0)
                override fun openFileOutput(p0: String?, p1: Int) = source.openFileOutput(p0, p1)
                override fun openOrCreateDatabase(p0: String?, p1: Int, p2: SQLiteDatabase.CursorFactory?) = source.openOrCreateDatabase(p0, p1, p2)
                override fun openOrCreateDatabase(p0: String?, p1: Int, p2: SQLiteDatabase.CursorFactory?, p3: DatabaseErrorHandler?) = source.openOrCreateDatabase(p0, p1, p2, p3)
                override fun peekWallpaper() = source.peekWallpaper()
                @SuppressLint("UnspecifiedRegisterReceiverFlag")
                override fun registerReceiver(p0: BroadcastReceiver?, p1: IntentFilter?) = source.registerReceiver(p0, p1)
                @RequiresApi(Build.VERSION_CODES.O)
                override fun registerReceiver(p0: BroadcastReceiver?, p1: IntentFilter?, p2: String?, p3: Handler?, p4: Int) = source.registerReceiver(p0, p1, p2, p3, p4)

                @RequiresPermission(Manifest.permission.BROADCAST_STICKY)
                override fun removeStickyBroadcast(p0: Intent?) = source.removeStickyBroadcast(p0)

                @RequiresPermission(allOf = [Manifest.permission.BROADCAST_STICKY, "android.permission.INTERACT_ACROSS_USERS"])
                override fun removeStickyBroadcastAsUser(p0: Intent?, p1: UserHandle?) = source.removeStickyBroadcastAsUser(p0, p1)
                override fun revokeUriPermission(p0: Uri?, p1: Int) = source.revokeUriPermission(p0, p1)
                @RequiresApi(Build.VERSION_CODES.O)
                override fun revokeUriPermission(p0: String?, p1: Uri?, p2: Int) = source.revokeUriPermission(p0, p1, p2)
                override fun sendBroadcast(p0: Intent?) = source.sendBroadcast(p0)
                override fun sendBroadcast(p0: Intent?, p1: String?) = source.sendBroadcast(p0, p1)

                @RequiresPermission("android.permission.INTERACT_ACROSS_USERS")
                override fun sendBroadcastAsUser(p0: Intent?, p1: UserHandle?) = source.sendBroadcastAsUser(p0, p1)
                @RequiresPermission("android.permission.INTERACT_ACROSS_USERS")
                override fun sendBroadcastAsUser(p0: Intent?, p1: UserHandle?, p2: String?) = source.sendBroadcastAsUser(p0, p1, p2)

                override fun sendOrderedBroadcast(p0: Intent?, p1: String?) = source.sendOrderedBroadcast(p0, p1)
                override fun sendOrderedBroadcast(p0: Intent, p1: String?, p2: BroadcastReceiver?, p3: Handler?, p4: Int, p5: String?, p6: Bundle?) = source.sendOrderedBroadcast(p0, p1, p2, p3, p4, p5, p6)
                @RequiresPermission("android.permission.INTERACT_ACROSS_USERS")
                override fun sendOrderedBroadcastAsUser(p0: Intent?, p1: UserHandle?, p2: String?, p3: BroadcastReceiver?, p4: Handler?, p5: Int, p6: String?, p7: Bundle?) = source.sendOrderedBroadcastAsUser(p0, p1, p2, p3, p4, p5, p6, p7)

                @RequiresPermission(Manifest.permission.BROADCAST_STICKY)
                override fun sendStickyBroadcast(p0: Intent?) = source.sendStickyBroadcast(p0)
                @RequiresPermission(allOf = [Manifest.permission.BROADCAST_STICKY, "android.permission.INTERACT_ACROSS_USERS"])
                override fun sendStickyBroadcastAsUser(p0: Intent?, p1: UserHandle?) = source.sendStickyBroadcastAsUser(p0, p1)
                @RequiresPermission(Manifest.permission.BROADCAST_STICKY)
                override fun sendStickyOrderedBroadcast(p0: Intent?, p1: BroadcastReceiver?, p2: Handler?, p3: Int, p4: String?, p5: Bundle?) = source.sendStickyOrderedBroadcast(p0, p1, p2, p3, p4, p5)
                @RequiresPermission(allOf = [Manifest.permission.BROADCAST_STICKY, "android.permission.INTERACT_ACROSS_USERS"])
                override fun sendStickyOrderedBroadcastAsUser(p0: Intent?, p1: UserHandle?, p2: BroadcastReceiver?, p3: Handler?, p4: Int, p5: String?, p6: Bundle?) = source.sendStickyOrderedBroadcastAsUser(p0, p1, p2, p3, p4, p5, p6)

                override fun setTheme(p0: Int) = source.setTheme(p0)
                override fun setWallpaper(p0: Bitmap?) = source.setWallpaper(p0)
                override fun setWallpaper(p0: InputStream?) = source.setWallpaper(p0)
                override fun startActivities(p0: Array<out Intent?>?) = source.startActivities(p0)
                override fun startActivities(p0: Array<out Intent?>?, p1: Bundle?) = source.startActivities(p0, p1)
                override fun startActivity(p0: Intent?) = source.startActivity(p0)
                override fun startActivity(p0: Intent?, p1: Bundle?) = source.startActivity(p0, p1)

                @RequiresApi(Build.VERSION_CODES.O)
                override fun startForegroundService(p0: Intent?) = source.startForegroundService(p0)
                override fun startInstrumentation(p0: ComponentName, p1: String?, p2: Bundle?) = source.startInstrumentation(p0, p1, p2)
                override fun startIntentSender(p0: IntentSender?, p1: Intent?, p2: Int, p3: Int, p4: Int) = source.startIntentSender(p0, p1, p2, p3, p4)
                override fun startIntentSender(p0: IntentSender?, p1: Intent?, p2: Int, p3: Int, p4: Int, p5: Bundle?) = source.startIntentSender(p0, p1, p2, p3, p4, p5)
                override fun startService(p0: Intent?) = source.startService(p0)
                override fun stopService(p0: Intent?) = source.stopService(p0)
                override fun unbindService(p0: ServiceConnection) = source.unbindService(p0)
                override fun unregisterReceiver(p0: BroadcastReceiver?) = source.unregisterReceiver(p0)
                @RequiresApi(Build.VERSION_CODES.O)
                override fun createContextForSplit(p0: String?) = source.createContextForSplit(p0)
                @RequiresApi(Build.VERSION_CODES.O)
                override fun registerReceiver(p0: BroadcastReceiver?, p1: IntentFilter?, p2: Int) = source.registerReceiver(p0, p1, p2)
                @SuppressLint("UnspecifiedRegisterReceiverFlag")
                @RequiresApi(Build.VERSION_CODES.O)
                override fun registerReceiver(p0: BroadcastReceiver?, p1: IntentFilter?, p2: String?, p3: Handler?) = source.registerReceiver(p0, p1, p2, p3)
            }
        }
    }
}

