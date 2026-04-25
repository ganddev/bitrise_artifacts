package de.ahlfeld.bitriseartifacts.builds.domain.repository

import de.ahlfeld.bitriseartifacts.builds.domain.model.BuildsPage

internal interface BuildsRepository {
    suspend fun getBuilds(appSlug: String, next: String? = null): BuildsPage
}
