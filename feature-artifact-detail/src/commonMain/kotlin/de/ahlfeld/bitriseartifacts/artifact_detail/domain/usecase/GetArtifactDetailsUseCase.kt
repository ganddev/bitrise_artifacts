package de.ahlfeld.bitriseartifacts.artifact_detail.domain.usecase

import de.ahlfeld.bitriseartifacts.artifact_detail.domain.model.ArtifactDetails
import de.ahlfeld.bitriseartifacts.artifact_detail.domain.repository.ArtifactsRepository

internal class GetArtifactDetailsUseCase(
    private val artifactsRepository: ArtifactsRepository
) {
    suspend operator fun invoke(
        appSlug: String,
        artifactSlug: String,
        buildSlug: String
    ): Result<ArtifactDetails> {
        try {
            val artifacts = artifactsRepository.getArtifactDetails(
                appSlug = appSlug, artifactSlug = artifactSlug, buildSlug = buildSlug
            )
            return Result.success(artifacts)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }
}

