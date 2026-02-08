package afc.musicapp

import android.Manifest
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
import androidx.activity.enableEdgeToEdge
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.core.content.ContextCompat
import androidx.core.net.toUri
import afc.musicapp.ui.App
import afc.musicapp.uistate.vm.MainVm
import org.kodein.di.DI
import org.kodein.di.DIAware
import org.kodein.di.bindProvider
import org.kodein.di.instance
import java.lang.String.format

class MainActivity : ComponentActivity(), DIAware {
    override val di by DI.lazy {
        val appDi = (applicationContext as DIAware).di
        extend(appDi)
        bindProvider<Context>(overrides = true) { this@MainActivity }
    }
    private val vm by instance<MainVm>()

    @OptIn(ExperimentalMaterial3Api::class)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            enableEdgeToEdge()
            App(di)
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
                Manifest.permission.WRITE_EXTERNAL_STORAGE
            ) == PackageManager.PERMISSION_GRANTED
        }
    }

    private fun requestPermissions(activity: Activity, requestCode: Int) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.R) {
            try {
                val intent = Intent(Settings.ACTION_MANAGE_APP_ALL_FILES_ACCESS_PERMISSION)
                intent.addCategory("android.intent.category.DEFAULT")
                intent.data = format("package:%s", activity.packageName).toUri()
                activity.startActivityForResult(intent, requestCode)
            } catch (_: Exception) {
                val intent = Intent().apply {
                    action = Settings.ACTION_MANAGE_ALL_FILES_ACCESS_PERMISSION
                }
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





