package de.ahlfeld.bitriseartifacts.artifacts.api.usecase

interface GetArtifactSlugsUseCase {
    suspend operator fun invoke(appSlug: String, buildSlug: String): List<String>
}
