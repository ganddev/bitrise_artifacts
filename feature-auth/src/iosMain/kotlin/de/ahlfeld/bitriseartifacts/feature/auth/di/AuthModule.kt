package de.ahlfeld.bitriseartifacts.feature.auth.di

import de.ahlfeld.bitriseartifacts.feature.auth.data.datastore.createDataStore
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformAuthModule: Module = module {
    single { createDataStore() }
}
