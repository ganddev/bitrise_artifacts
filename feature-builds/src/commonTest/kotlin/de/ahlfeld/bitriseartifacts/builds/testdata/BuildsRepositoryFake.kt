package de.ahlfeld.bitriseartifacts.builds.testdata

import de.ahlfeld.bitriseartifacts.builds.domain.model.Build
import de.ahlfeld.bitriseartifacts.builds.domain.repository.BuildsRepository

internal class BuildsRepositoryFake :
    BuildsRepository {
    var result: List<Build>? = null

    var exception : Exception? = null

    override suspend fun getBuilds(appSlug: String): List<Build> {
        exception?.let {
            throw it
        }
        return result!!

    }
}