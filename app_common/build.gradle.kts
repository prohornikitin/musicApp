import com.android.build.api.dsl.KotlinMultiplatformAndroidLibraryTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.composeMultiplatform)
    alias(libs.plugins.androidKotlinMultiplatformLibrary)
    alias(libs.plugins.composeCompiler)
//    alias(libs.plugins.composeHotReload)
    alias(libs.plugins.kotlin.serialization)
    id("com.google.devtools.ksp")
}

kotlin {
    jvm()
    jvmToolchain(17)
    listOf(
        iosX64(),
        iosArm64(),
        iosSimulatorArm64()
    ).forEach {
        it.binaries.framework {
            baseName = "commonMain"
            isStatic = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(libs.kotlin.stdlib)
            implementation(project(":domain"))
            implementation(project(":data"))
            implementation(libs.compose.runtime)
            implementation(libs.compose.ui)
            implementation(libs.compose.foundation)
            implementation(libs.compose.resources)
            implementation(libs.androidx.datastore)
            implementation(project.dependencies.platform(libs.androidx.compose.bom))
            implementation(libs.androidx.material3)
            implementation(libs.androidx.material3.icons)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.navigation.compose)
            implementation(libs.kodein.core)
            implementation(libs.kodein.compose)
            implementation(libs.kotlinx.coroutines.core)
            api(libs.androidx.lifecycle.viewmodel)
//            androidTestImplementation(libs.androidx.junit)
//            androidTestImplementation(libs.androidx.espresso.core)
//            debugImplementation(libs.androidx.ui.tooling)
//            debugImplementation(libs.androidx.ui.test.manifest)
//            implementation(libs.hilt.android.v2561)
//            ksp(libs.hilt.android.compiler)
//            implementation(libs.coil)
        }
        commonTest.dependencies {
            implementation(libs.kotlin.test)
        }
        jvmMain.dependencies {
            implementation(compose.desktop.currentOs)
            implementation(libs.kotlinx.coroutines.swing)
        }
    }

    configure<KotlinMultiplatformAndroidLibraryTarget> {
        namespace = "afc.musicapp.app_common"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()
        androidResources.enable = true
    }
}