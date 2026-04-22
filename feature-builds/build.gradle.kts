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
            baseName = "Builds"
            isStatic = true
        }
    }

    android {
        namespace = "de.ahlfeld.bitriseartifacts.builds"
        androidResources {
            enable = true
        }
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.featureBuildsApi)
            implementation(projects.featureArtifactsApi)
            implementation(projects.featureArtifactDetailApi)
            implementation(libs.compose.material.icons.extended)
        }
    }
}
