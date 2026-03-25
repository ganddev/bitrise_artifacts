package de.ahlfeld.bitriseartifacts.builds.domain.usecase

import de.ahlfeld.bitriseartifacts.builds.domain.model.Build
import de.ahlfeld.bitriseartifacts.builds.domain.repository.BuildsRepository

internal class GetBuildsUseCase(private val repository: BuildsRepository) {
    suspend operator fun invoke(appSlug: String): Result<List<Build>> = runCatching {
        repository.getBuilds(appSlug)
    }
}
