import org.jetbrains.kotlin.gradle.ExperimentalKotlinGradlePluginApi
import org.jetbrains.kotlin.gradle.dsl.JvmTarget

plugins {
    alias(libs.plugins.kotlinMultiplatform)
    alias(libs.plugins.androidKmpLibraryPluging)
    alias(libs.plugins.kotlinSerialization)
    alias(libs.plugins.bitrise.kmp.network)
    alias(libs.plugins.bitrise.kmp.compose)
    alias(libs.plugins.bitrise.kmp.koin)
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Apps"
            isStatic = true
        }
    }

    jvm()

    android {
        namespace = "de.ahlfeld.bitriseartifacts.apps"
        compileSdk = libs.versions.android.compileSdk.get().toInt()
        minSdk = libs.versions.android.minSdk.get().toInt()

        @OptIn(ExperimentalKotlinGradlePluginApi::class)
        compilerOptions {
            jvmTarget.set(JvmTarget.JVM_17)
        }

        androidResources {
            enable = true
        }
    }

    sourceSets {
        commonTest.dependencies {
            implementation(libs.kotlin.test)
            implementation(libs.kotlinx.coroutines.test)
        }
        commonMain.dependencies {
            implementation(projects.featureAppsApi)
            implementation(projects.featureAuthApi)
            implementation(projects.featureBuildsApi)
            implementation(libs.kotlinx.coroutines.core)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
            implementation(libs.jetbrains.navigation.compose)
        }
    }
}
