package de.ahlfeld.bitriseartifacts.builds.di

import de.ahlfeld.bitriseartifacts.builds.data.repository.BitriseBuildsRepository
import de.ahlfeld.bitriseartifacts.builds.domain.repository.BuildsRepository
import de.ahlfeld.bitriseartifacts.builds.domain.usecase.GetBuildsUseCase
import de.ahlfeld.bitriseartifacts.builds.presentation.BuildsViewModelImpl
import de.ahlfeld.bitriseartifacts.builds.presentation.BuildsViewModel
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val buildsModule = module {
    singleOf(::BitriseBuildsRepository) bind BuildsRepository::class
    factoryOf(::GetBuildsUseCase)
    factory { (appSlug: String) -> BuildsViewModelImpl(appSlug, get()) } bind BuildsViewModel::class
}
