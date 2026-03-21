dependencyResolutionManagement {
    repositories {
        google()
        mavenCentral()
        gradlePluginPortal()
    }
    versionCatalogs {
        create("libs") {
            from(files("../gradle/libs.versions.toml"))
        }
    }
}

plugins {
    id("dev.panuszewski.typesafe-conventions") version "0.10.0"
}

rootProject.name = "build-logic"
include(":convention")
