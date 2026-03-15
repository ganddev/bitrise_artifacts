package de.ahlfeld.bitriseartifacts.di

import org.koin.core.context.startKoin
import org.koin.dsl.KoinAppDeclaration

fun initKoin(appDeclaration: KoinAppDeclaration = {}) =
    startKoin {
        appDeclaration()
        modules(networkModule, appModule)
    }

// ios
fun initKoin() = initKoin {}
