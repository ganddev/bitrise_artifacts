package de.ahlfeld.bitriseartifacts.apps.data

import de.ahlfeld.bitriseartifacts.apps.data.repository.BitriseAppsRepository
import de.ahlfeld.bitriseartifacts.apps.domain.model.App
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
import kotlin.test.assertFails

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
    fun `getApps returns list of apps from valid json`() = runTest {
        val json = """
            {
              "data": [
                {
                  "slug": "app-1",
                  "title": "Title 1",
                  "owner": { "name": "Owner 1" },
                  "avatar_url": "url1"
                },
                {
                  "slug": "app-2",
                  "title": "Title 2",
                  "owner": { "name": "Owner 2" },
                  "avatar_url": null
                }
              ],
              "paging": { "total_item_count": 2, "page_item_limit": 50 }
            }
        """.trimIndent()
        
        val httpClient = createMockHttpClient(json)
        val repository = BitriseAppsRepository(httpClient)
        val result = repository.getApps()

        assertEquals(2, result.size)
        assertEquals("Title 1", result[0].title)
        assertEquals("Owner 1", result[0].ownerName)
        assertEquals("url1", result[0].avatarUrl)
        
        assertEquals("Title 2", result[1].title)
        assertEquals("Owner 2", result[1].ownerName)
        assertEquals(null, result[1].avatarUrl)
    }

    @Test
    fun `getApps returns empty list when data is empty`() = runTest {
        val json = """{ "data": [], "paging": { "total_item_count": 0, "page_item_limit": 50 } }"""
        val httpClient = createMockHttpClient(json)
        val repository = BitriseAppsRepository(httpClient)
        val result = repository.getApps()

        assertEquals(0, result.size)
    }

    @Test
    fun `getApps throws exception on 401 Unauthorized`() = runTest {
        val httpClient = createMockHttpClient("Unauthorized", HttpStatusCode.Unauthorized)
        val repository = BitriseAppsRepository(httpClient)

        assertFails {
            repository.getApps()
        }
    }

    @Test
    fun `getApps throws exception on 404 Not Found`() = runTest {
        val httpClient = createMockHttpClient("Not Found", HttpStatusCode.NotFound)
        val repository = BitriseAppsRepository(httpClient)

        assertFails {
            repository.getApps()
        }
    }

    @Test
    fun `getApps throws exception on 500 Server Error`() = runTest {
        val httpClient = createMockHttpClient("Error", HttpStatusCode.InternalServerError)
        val repository = BitriseAppsRepository(httpClient)

        assertFails {
            repository.getApps()
        }
    }
}
