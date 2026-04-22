plugins {
    id("bitriseartifacts.feature")
    id("bitriseartifacts.koin")
    id("bitriseartifacts.ktor")
    id("bitriseartifacts.compose.multiplatform")
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

    android {
        namespace = "de.ahlfeld.bitriseartifacts.apps"
        androidResources {
            enable = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.featureAppsApi)
            implementation(projects.featureAuthApi)
            implementation(projects.featureBuildsApi)
            implementation(libs.coil.compose)
            implementation(libs.coil.network.ktor)
        }
    }
}
