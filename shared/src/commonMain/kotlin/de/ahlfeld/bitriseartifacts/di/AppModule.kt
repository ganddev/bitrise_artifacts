package de.ahlfeld.bitriseartifacts.di

import de.ahlfeld.bitriseartifacts.apps.data.repository.BitriseAppsRepository
import de.ahlfeld.bitriseartifacts.apps.domain.repository.AppsRepository
import de.ahlfeld.bitriseartifacts.apps.domain.usecase.GetAppsUseCase
import de.ahlfeld.bitriseartifacts.apps.presentation.AppsViewModel
import de.ahlfeld.bitriseartifacts.apps.presentation.AppsViewModelImpl
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

val appModule = module {
    singleOf(::BitriseAppsRepository) bind AppsRepository::class
    factoryOf(::GetAppsUseCase)
    factoryOf(::AppsViewModelImpl) bind AppsViewModel::class
}
