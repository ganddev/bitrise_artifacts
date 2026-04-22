plugins {
    `kotlin-dsl`
}

group = "de.ahlfeld.bitriseartifacts.buildlogic"

dependencies {
    implementation(libs.android.gradlePlugin)
    implementation(libs.kotlin.gradlePlugin)
    implementation(libs.kotlin.serialization.gradlePlugin)
    implementation(libs.compose.gradlePlugin)
}

gradlePlugin {
    plugins {
        register("ktor") {
            id = "bitriseartifacts.ktor"
            implementationClass = "KtorConventionPlugin"
        }
        register("composeMultiplatform") {
            id = "bitriseartifacts.compose.multiplatform"
            implementationClass = "ComposeMultiplatformConventionPlugin"
        }
        register("koin") {
            id = "bitriseartifacts.koin"
            implementationClass = "KoinConventionPlugin"
        }
        register("feature") {
            id = "bitriseartifacts.feature"
            implementationClass = "FeatureConventionPlugin"
        }
    }
}
