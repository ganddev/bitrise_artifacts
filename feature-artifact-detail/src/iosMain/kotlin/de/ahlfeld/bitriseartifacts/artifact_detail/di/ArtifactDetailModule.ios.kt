package de.ahlfeld.bitriseartifacts.artifact_detail.di

import de.ahlfeld.bitriseartifacts.artifact_detail.domain.service.UrlOpener
import de.ahlfeld.bitriseartifacts.artifact_detail.domain.service.UrlOpenerImpl
import org.koin.core.module.Module
import org.koin.dsl.bind
import org.koin.dsl.module

actual fun urlOpenerModule(): Module = module {
    factory<UrlOpener> { UrlOpenerImpl() } bind UrlOpener::class
}

