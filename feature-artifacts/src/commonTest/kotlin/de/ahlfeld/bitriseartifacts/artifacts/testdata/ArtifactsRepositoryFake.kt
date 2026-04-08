package de.ahlfeld.bitriseartifacts.artifacts.testdata

import de.ahlfeld.bitriseartifacts.artifacts.api.model.Artifact
import de.ahlfeld.bitriseartifacts.artifacts.domain.repository.ArtifactsRepository

internal class ArtifactsRepositoryFake(
    var artifacts: List<Artifact> = emptyList()
) : ArtifactsRepository {
    override suspend fun getArtifacts(appSlug: String, buildSlug: String): List<Artifact> =
        artifacts
}