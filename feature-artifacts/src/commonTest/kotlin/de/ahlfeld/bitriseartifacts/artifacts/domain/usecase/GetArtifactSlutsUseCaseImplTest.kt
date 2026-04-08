package de.ahlfeld.bitriseartifacts.artifacts.domain.usecase

import de.ahlfeld.bitriseartifacts.artifacts.api.model.Artifact
import de.ahlfeld.bitriseartifacts.artifacts.testdata.ArtifactsRepositoryFake
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

class GetArtifactSlutsUseCaseImplTest {

    private val repository = ArtifactsRepositoryFake()
    private val useCase = GetArtifactSlutsUseCaseImpl(repository)


    @Test
    fun `invoke returns empty list when no artifacts`() = runTest {
        repository.artifacts = emptyList()

        val result = useCase(appSlug = "appSlug", "buildSlug")

        assertEquals(emptyList(), result)
    }

    @Test
    fun `invoke returns list of artifact sluts when repository returns list of artifacts`() = runTest {
        repository.artifacts = List(
            5
        ) { index ->
            Artifact(
                slug = "artifact${index}",
                title = "Title $index",
                artifactType = "type${index}",
                publicInstallPageUrl = if (index % 2 == 0) "http://public${index}.url" else ""
            )
        }

        val result = useCase(appSlug = "appSlug", buildSlug = "buildSlug")

        assertEquals(
            expected = listOf(
                "artifact0", "artifact1", "artifact2", "artifact3", "artifact4",
            ),
            actual = result
        )
    }
}
