package de.ahlfeld.bitriseartifacts.builds.domain.repository

import de.ahlfeld.bitriseartifacts.builds.domain.model.Build

internal interface BuildsRepository {
    suspend fun getBuilds(appSlug: String): List<Build>
}
