package de.ahlfeld.bitriseartifacts.artifacts.domain.usecase

import de.ahlfeld.bitriseartifacts.artifacts.domain.repository.ArtifactsRepository
import de.ahlfeld.bitriseartifacts.artifacts.api.usecase.GetArtifactSlugsUseCase

internal class GetArtifactSlugsUseCaseImpl(
    private val repository: ArtifactsRepository
) : GetArtifactSlugsUseCase {
    override suspend fun invoke(appSlug: String, buildSlug: String): List<String> {
        val artifacts = repository.getArtifacts(appSlug, buildSlug)
        return artifacts.map { it.slug }
    }
}
