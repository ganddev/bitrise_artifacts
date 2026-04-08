package de.ahlfeld.bitriseartifacts.artifact_detail.di

import de.ahlfeld.bitriseartifacts.artifact_detail.data.BitriseArtifactsRepository
import de.ahlfeld.bitriseartifacts.artifact_detail.domain.repository.ArtifactsRepository
import de.ahlfeld.bitriseartifacts.artifact_detail.domain.usecase.GetArtifactDetailsUseCase
import de.ahlfeld.bitriseartifacts.artifact_detail.presentation.ArtifactDetailsViewModel
import de.ahlfeld.bitriseartifacts.artifact_detail.presentation.ArtifactDetailsViewModelImpl
import org.koin.core.module.Module
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.bind
import org.koin.dsl.module

val artifactDetailModule = module {
    includes(urlOpenerModule())
    factory<ArtifactsRepository> { BitriseArtifactsRepository(get()) }
    factory<GetArtifactDetailsUseCase> { GetArtifactDetailsUseCase(get()) }
    viewModel {
        ArtifactDetailsViewModelImpl(
            savedStateHandle = get(),
            getArtifactDetails = get(),
        )
    } bind ArtifactDetailsViewModel::class
}

expect fun urlOpenerModule(): Module