import org.jetbrains.compose.desktop.application.dsl.TargetFormat

plugins {
//    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.composeCompiler)
    alias(libs.plugins.androidApplication)
    alias(libs.plugins.kotlin.serialization)
}

dependencies {
    implementation(libs.androidx.activity.compose)
    implementation(libs.androidx.core.ktx)
    implementation(libs.androidx.lifecycle.runtime.ktx)
    implementation(libs.media3.exoplayer)
    implementation(libs.media3.exoplayer.dash)
    implementation(libs.media3.session)
    implementation(libs.media3.exoplayer.dash)

    implementation(project(":domain"))
    implementation(project(":app_common"))
    implementation(libs.androidx.datastore)
    implementation(libs.androidx.material3)
    implementation(libs.androidx.material3.icons)
    implementation(libs.compose.runtime)
    implementation(libs.compose.ui)
    implementation(libs.compose.foundation)
    implementation(libs.rwLock)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.navigation.compose)
    implementation(libs.kodein.core)
    testImplementation(libs.kotlin.test)
}

//jvmMain.dependencies {
//    implementation(compose.desktop.currentOs)
//    implementation(libs.kotlinx.coroutines.swing)
//}
//iosArm64Main.dependencies {
//    implementation(libs.kodein.iosArm64)
//}
//iosX64Main.dependencies {
//    implementation(libs.kodein.iosX64)
//}
//iosSimulatorArm64Main.dependencies {
//    implementation(libs.kodein.iosSimulatorArm64)
//}

//compose.desktop {
//    application {
//        mainClass = "MainKt"
//        nativeDistributions {
//            targetFormats(TargetFormat.Dmg, TargetFormat.Msi, TargetFormat.Deb)
//            packageName = "sample"
//            packageVersion = "1.0.0"
//        }
//    }
//}

android {
    namespace = "afc.musicapp"
    compileSdk = libs.versions.android.compileSdk.get().toInt()

    defaultConfig {
        applicationId = "afc.musicapp"
        minSdk = libs.versions.android.minSdk.get().toInt()
        targetSdk = libs.versions.android.targetSdk.get().toInt()
        versionCode = 1
        versionName = "1.0"

        testInstrumentationRunner = "androidx.test.runner.AndroidJUnitRunner"
    }

    packaging {
        resources {
            excludes += "/META-INF/{AL2.0,LGPL2.1}"
        }
    }
    buildTypes {
        getByName("release") {
            isMinifyEnabled = false
            signingConfig = signingConfigs.getByName("debug")
        }
    }
    compileOptions {
        sourceCompatibility = JavaVersion.VERSION_17
        targetCompatibility = JavaVersion.VERSION_17
    }
}



