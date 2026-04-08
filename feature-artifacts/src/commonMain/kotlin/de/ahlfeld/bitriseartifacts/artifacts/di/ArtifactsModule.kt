package de.ahlfeld.bitriseartifacts.artifacts.di

import de.ahlfeld.bitriseartifacts.artifacts.api.usecase.GetArtifactSlugsUseCase
import de.ahlfeld.bitriseartifacts.artifacts.data.repository.BitriseArtifactsRepository
import de.ahlfeld.bitriseartifacts.artifacts.domain.repository.ArtifactsRepository
import de.ahlfeld.bitriseartifacts.artifacts.domain.usecase.GetArtifactSlugsUseCaseImpl
import org.koin.dsl.module

val artifactsModule = module {
    single<ArtifactsRepository> { BitriseArtifactsRepository(get()) }
    factory<GetArtifactSlugsUseCase> { GetArtifactSlugsUseCaseImpl(get()) }
}
