package de.ahlfeld.bitriseartifacts.apps.testdata

import de.ahlfeld.bitriseartifacts.apps.domain.model.App
import de.ahlfeld.bitriseartifacts.apps.domain.repository.AppsRepository

internal class AppsRepositoryFake : AppsRepository {
    var result: List<App>? = null
    var exception: Throwable? = null

    override suspend fun getApps(): List<App> {
        exception?.let { throw it }
        return result ?: emptyList()
    }
}