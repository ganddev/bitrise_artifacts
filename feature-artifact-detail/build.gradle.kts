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
            baseName = "ArtifactDetail"
            isStatic = true
        }
    }

    android {
        namespace = "de.ahlfeld.bitriseartifacts.artifact_detail"
    }

    sourceSets {
        androidMain.dependencies {
            implementation(libs.androidx.browser)
        }
        commonMain.dependencies {
            implementation(projects.featureArtifactDetailApi)
            implementation(projects.featureArtifactsApi)
            implementation(libs.compose.material.icons.extended)
        }
    }
}
