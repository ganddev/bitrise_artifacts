package de.ahlfeld.bitriseartifacts.builds.domain.usecase

import de.ahlfeld.bitriseartifacts.builds.domain.model.Build
import de.ahlfeld.bitriseartifacts.builds.domain.repository.BuildsRepository
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
        val repository = object : BuildsRepository {
            override suspend fun getBuilds(appSlug: String): List<Build> = builds
        }
        val useCase = GetBuildsUseCase(repository)

        val result = useCase("app-slug")

        assertTrue(result.isSuccess)
        assertEquals(builds, result.getOrNull())
    }

    @Test
    fun `invoke returns failure when repository throws exception`() = runTest {
        val exception = Exception("Network error")
        val repository = object : BuildsRepository {
            override suspend fun getBuilds(appSlug: String): List<Build> {
                throw exception
            }
        }
        val useCase = GetBuildsUseCase(repository)

        val result = useCase("app-slug")

        assertTrue(result.isFailure)
        assertEquals(exception, result.exceptionOrNull())
    }
}
