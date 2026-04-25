package de.ahlfeld.bitriseartifacts.builds.domain.usecase

import de.ahlfeld.bitriseartifacts.builds.domain.model.BuildsPage
import de.ahlfeld.bitriseartifacts.builds.domain.repository.BuildsRepository

internal class GetBuildsUseCase(private val repository: BuildsRepository) {
    suspend operator fun invoke(appSlug: String, next: String? = null): Result<BuildsPage> = runCatching {
        repository.getBuilds(appSlug, next)
    }
}
