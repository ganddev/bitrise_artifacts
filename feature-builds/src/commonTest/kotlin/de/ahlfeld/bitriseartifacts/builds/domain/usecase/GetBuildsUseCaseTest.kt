package de.ahlfeld.bitriseartifacts.builds.domain.usecase

import de.ahlfeld.bitriseartifacts.builds.domain.model.Build
import de.ahlfeld.bitriseartifacts.builds.domain.model.BuildsPage
import de.ahlfeld.bitriseartifacts.builds.domain.repository.BuildsRepository
import de.ahlfeld.bitriseartifacts.builds.testdata.BuildsRepositoryFake
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class GetBuildsUseCaseTest {

    @Test
    fun `invoke returns success when repository returns builds`() = runTest {
        val builds = listOf(
            Build(1, "main", "2023-01-01T10:00:00Z", null, "hash1", "slug1", 1)
        )
        val buildsPage = BuildsPage(builds, null)
        val repository = BuildsRepositoryFake().apply {
            result = buildsPage
        }
        val useCase = GetBuildsUseCase(repository)

        val result = useCase("app-slug")

        assertTrue(result.isSuccess)
        assertEquals(buildsPage, result.getOrNull())
    }

    @Test
    fun `invoke returns failure when repository throws exception`() = runTest {
        val exception = Exception("Network error")
        val repository = BuildsRepositoryFake().apply {
            this.exception = exception
        }
        val useCase = GetBuildsUseCase(repository)

        val result = useCase("app-slug")

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
