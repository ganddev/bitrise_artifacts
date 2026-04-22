plugins {
    id("bitriseartifacts.feature")
    id("bitriseartifacts.ktor")
    id("bitriseartifacts.koin")
}

kotlin {
    listOf(
        iosArm64(),
        iosSimulatorArm64()
    ).forEach { iosTarget ->
        iosTarget.binaries.framework {
            baseName = "Artifacts"
            isStatic = true
        }
    }

    android {
        namespace = "de.ahlfeld.bitriseartifacts.artifacts"
    }

    sourceSets {
        commonMain.dependencies {
            implementation(projects.featureArtifactsApi)
        }
        commonTest.dependencies {
            implementation(libs.ktor.client.mock)
        }
    }
}
