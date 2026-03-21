package de.ahlfeld.bitriseartifacts.di

import de.ahlfeld.bitriseartifacts.feature.auth.di.authModule
import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(authModule, networkModule, appModule)
    }

// ios
fun initKoin() = initKoin {}
