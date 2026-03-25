package de.ahlfeld.bitriseartifacts.artifacts.api.usecase

import de.ahlfeld.bitriseartifacts.artifacts.api.model.Artifact

interface GetArtifactsUseCase {
    suspend operator fun invoke(
        appSlug: String,
        artifactSlug: String,
        buildSlug: String,
    ): Artifact
}