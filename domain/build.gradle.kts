plugins {
    id("java-library")
    alias(libs.plugins.jetbrains.kotlin.jvm)
    id("com.google.devtools.ksp")
}
java {
    sourceCompatibility = JavaVersion.VERSION_11
    targetCompatibility = JavaVersion.VERSION_11
}
kotlin {
    compilerOptions {
        jvmTarget = org.jetbrains.kotlin.gradle.dsl.JvmTarget.JVM_11
    }
}
dependencies {
    implementation(project(":config"))
    implementation(libs.dagger)
    ksp(libs.dagger.compiler)
    testImplementation(libs.junit)
    implementation(libs.kotlinx.serialization.json)
    implementation(libs.kotlinx.coroutines.core)
}
