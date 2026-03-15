package de.ahlfeld.bitriseartifacts.data.repository

import de.ahlfeld.bitriseartifacts.domain.model.App
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

    private val jsonContent = """
        {
          "data": [
            {
              "slug": "app-slug-1",
              "title": "App Title 1",
              "owner": {
                "name": "Owner Name 1"
              },
              "avatar_url": "https://example.com/avatar1.png"
            }
          ],
          "paging": {
            "total_item_count": 1,
            "page_item_limit": 50
          }
        }
    """.trimIndent()

    @Test
    fun `getApps returns list of BitriseApp`() = runTest {
        val mockEngine = MockEngine { request ->
            respond(
                content = jsonContent,
                status = HttpStatusCode.OK,
                headers = headersOf(HttpHeaders.ContentType, ContentType.Application.Json.toString())
            )
        }

        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }

        val repository = BitriseAppsRepository(httpClient)
        val result = repository.getApps()

        val expected = listOf(
            App(
                slug = "app-slug-1",
                title = "App Title 1",
                ownerName = "Owner Name 1",
                avatarUrl = "https://example.com/avatar1.png"
            )
        )

        assertEquals(expected, result)
    }
}
