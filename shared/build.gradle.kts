import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibraryPluging)
    alias(libs.plugins.bitrise.kmp.network)
    alias(libs.plugins.bitrise.kmp.compose)
    alias(libs.plugins.bitrise.kmp.koin)
    alias(libs.plugins.kotlinSerialization)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "shared"
            isStatic = true
        }
    }

    jvm()

    android {
        namespace = "de.ahlfeld.bitriseartifacts.shared"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.featureApps)
            implementation(projects.featureAppsApi)
            implementation(projects.featureAuth)
            implementation(projects.featureAuthApi)
            implementation(projects.featureBuildsApi)
            implementation(projects.featureBuilds)
            
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.kotlinx.serialization.json)
            implementation(libs.jetbrains.navigation.compose)
        }
    }
}
