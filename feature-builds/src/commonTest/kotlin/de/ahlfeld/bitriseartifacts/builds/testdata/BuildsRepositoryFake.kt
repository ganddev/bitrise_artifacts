package de.ahlfeld.bitriseartifacts.builds.testdata

import de.ahlfeld.bitriseartifacts.builds.domain.model.BuildsPage
import de.ahlfeld.bitriseartifacts.builds.domain.repository.BuildsRepository

internal class BuildsRepositoryFake : BuildsRepository {
    var result: BuildsPage? = null
    var exception: Exception? = null

    override suspend fun getBuilds(appSlug: String, next: String?): BuildsPage {
        exception?.let { throw it }
        return result!!
    }
}
