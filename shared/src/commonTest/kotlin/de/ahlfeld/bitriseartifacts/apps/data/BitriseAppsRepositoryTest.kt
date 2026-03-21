package de.ahlfeld.bitriseartifacts.apps.data

import de.ahlfeld.bitriseartifacts.apps.data.repository.BitriseAppsRepository
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
import kotlinx.serialization.json.Json
import kotlin.test.Test
import kotlin.test.assertEquals

class BitriseAppsRepositoryTest {

    private fun createMockHttpClient(content: String, status: HttpStatusCode = HttpStatusCode.OK): HttpClient {
        val mockEngine = MockEngine { _ ->
            respond(
                content = content,
                status = status,
                headers = headersOf(
                    HttpHeaders.ContentType,
                    ContentType.Application.Json.toString()
                )
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
    fun `getApps returns list of apps when status is 200 OK`() = runTest {
        val json = """
            {
              "data": [
                {
                  "slug": "app-1",
                  "title": "Title 1",
                  "owner": { "name": "Owner 1" },
                  "avatar_url": "url1"
                }
              ],
              "paging": { "total_item_count": 1, "page_item_limit": 50 }
            }
        """.trimIndent()
        
        val httpClient = createMockHttpClient(json, HttpStatusCode.OK)
        val repository = BitriseAppsRepository(httpClient)
        val result = repository.getApps()

        assertEquals(1, result.size)
        assertEquals("Title 1", result[0].title)
    }

    @Test
    fun `getApps returns empty list when status is not 200 OK`() = runTest {
        val httpClient = createMockHttpClient("Unauthorized", HttpStatusCode.Unauthorized)
        val repository = BitriseAppsRepository(httpClient)
        val result = repository.getApps()

        assertEquals(0, result.size)
    }

    @Test
    fun `getApps returns empty list when status is 500 Internal Server Error`() = runTest {
        val httpClient = createMockHttpClient("Server Error", HttpStatusCode.InternalServerError)
        val repository = BitriseAppsRepository(httpClient)
        val result = repository.getApps()

        assertEquals(0, result.size)
    }
}
