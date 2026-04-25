package de.ahlfeld.bitriseartifacts.builds.data.repository

import de.ahlfeld.bitriseartifacts.builds.data.model.BuildDto
import de.ahlfeld.bitriseartifacts.builds.data.model.BuildsResponseDto
import de.ahlfeld.bitriseartifacts.builds.data.model.PagingDto
import io.ktor.client.HttpClient
import io.ktor.client.engine.mock.MockEngine
import io.ktor.client.engine.mock.respond
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpStatusCode
import io.ktor.http.headersOf
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.test.runTest
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

class BitriseBuildsRepositoryTest {

    private fun createMockHttpClient(
        status: HttpStatusCode,
        content: String = ""
    ): HttpClient {
        val mockEngine = MockEngine {
            respond(
                content = content,
                status = status,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }
        return HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
    }

    @Test
    fun `getBuilds returns list of builds when status is OK`() = runTest {
        val responseDto = BuildsResponseDto(
            data = listOf(
                BuildDto(
                    buildNumber = 1,
                    branch = "main",
                    triggeredAt = "2023-01-01T10:00:00Z",
                    slug = "slug1",
                    status = 1
                )
            ),
            paging = PagingDto(1, 50)
        )
        val httpClient = createMockHttpClient(
            status = HttpStatusCode.OK,
            content = Json.encodeToString(responseDto)
        )
        val repository = BitriseBuildsRepository(httpClient)

        val result = repository.getBuilds("app-slug")

        assertEquals(1, result.builds.size)
        assertEquals(1, result.builds[0].buildNumber)
        assertEquals("main", result.builds[0].branch)
    }

    @Test
    fun `getBuilds returns empty list when status is not OK`() = runTest {
        val httpClient = createMockHttpClient(
            status = HttpStatusCode.InternalServerError
        )
        val repository = BitriseBuildsRepository(httpClient)

        val result = repository.getBuilds("app-slug")

        assertTrue(result.builds.isEmpty())
    }
}
