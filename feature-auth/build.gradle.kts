plugins {
    id("bitriseartifacts.feature")
    id("bitriseartifacts.compose.multiplatform")
    id("bitriseartifacts.koin")
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "featureAuth"
            isStatic = true
        }
    }

    android {
        namespace = "de.ahlfeld.bitriseartifacts.feature.auth"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.featureAuthApi)
            implementation(libs.datastore.preferences.core)
        }
        androidMain.dependencies {
            implementation(libs.datastore.preferences.android)
        }
    }
}
