package de.ahlfeld.bitriseartifacts.di

import de.ahlfeld.bitriseartifacts.apps.di.appsModule
import de.ahlfeld.bitriseartifacts.artifact_detail.di.artifactDetailModule
import de.ahlfeld.bitriseartifacts.artifacts.di.artifactsModule
import de.ahlfeld.bitriseartifacts.builds.di.buildsModule
import org.koin.dsl.module

val appModule = module {
    includes(appsModule, buildsModule, artifactsModule, artifactDetailModule)
}
