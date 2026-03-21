package de.ahlfeld.bitriseartifacts.feature.auth.di

import de.ahlfeld.bitriseartifacts.feature.auth.data.repository.DataStoreAuthRepository
import de.ahlfeld.bitriseartifacts.feature.auth.domain.repository.AuthRepository
import de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase.GetTokenUseCase
import de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase.SaveTokenUseCase
import de.ahlfeld.bitriseartifacts.feature.auth.presentation.AuthViewModel
import org.koin.core.module.Module
import org.koin.core.module.dsl.factoryOf
import org.koin.core.module.dsl.singleOf
import org.koin.dsl.bind
import org.koin.dsl.module

expect val platformAuthModule: Module

val authModule = module {
    includes(platformAuthModule)
    singleOf(::DataStoreAuthRepository) bind AuthRepository::class
    factoryOf(::GetTokenUseCase)
    factoryOf(::SaveTokenUseCase)
    factoryOf(::AuthViewModel)
}
