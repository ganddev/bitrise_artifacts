package de.ahlfeld.bitriseartifacts.builds.testdata

import de.ahlfeld.bitriseartifacts.artifacts.api.usecase.GetArtifactSlugsUseCase

class GetArtifactSlugsUseCaseFake : GetArtifactSlugsUseCase {
    var result : List<String> = emptyList()

    override suspend fun invoke(appSlug: String, buildSlug: String): List<String> {
        return result
    }
}
