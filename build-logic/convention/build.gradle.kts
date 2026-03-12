plugins {
    `kotlin-dsl`
}

group = "de.ahlfeld.bitriseartifacts.buildlogic"

dependencies {
    compileOnly(libs.android.gradlePlugin)
    compileOnly(libs.kotlin.gradlePlugin)
    compileOnly(libs.compose.gradlePlugin)
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
    }
}
