rootProject.name = "BitriseArtifacts"
enableFeaturePreview("TYPESAFE_PROJECT_ACCESSORS")

includeBuild("build-logic")

pluginManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
        maven("https://maven.pkg.jetbrains.com/public")
    }
}

dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        maven("https://maven.pkg.jetbrains.com/public")
    }
}

include(":androidApp")
include(":feature-apps")
include(":feature-apps-api")
include(":feature-auth")
include(":feature-auth-api")
include(":feature-builds")
include(":shared")
include(":feature-builds-api")
include(":feature-artifacts")
include(":feature-artifacts-api")
include(":feature-artifact-detail")
include(":feature-artifact-detail-api")
