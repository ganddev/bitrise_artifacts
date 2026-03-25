package de.ahlfeld.bitriseartifacts.artifacts.data.repository

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
import kotlin.test.assertTrue

class BitriseArtifactsRepositoryTest {

    private fun createRepository(
        responses: Map<String, String> = emptyMap(),
        defaultStatus: HttpStatusCode = HttpStatusCode.OK
    ): BitriseArtifactsRepository {
        val mockEngine = MockEngine { request ->
            val url = request.url.encodedPath
            val content = responses.entries.find { url.endsWith(it.key) }?.value
            if (content != null) {
                respond(
                    content = content,
                    status = HttpStatusCode.OK,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        ContentType.Application.Json.toString()
                    )
                )
            } else {
                respond(
                    content = "",
                    status = defaultStatus,
                    headers = headersOf(
                        HttpHeaders.ContentType,
                        ContentType.Application.Json.toString()
                    )
                )
            }
        }
        val httpClient = HttpClient(mockEngine) {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                })
            }
        }
        return BitriseArtifactsRepository(httpClient)
    }

    @Test
    fun `getArtifacts returns list of artifacts with details on success`() = runTest {
        val listContent = """
            {
                "data": [
                    {
                        "title": "Artifact 1",
                        "artifact_type": "android-apk",
                        "slug": "slug1",
                        "is_public_page_enabled": true
                    }
                ]
            }
        """.trimIndent()
        val detailContent = """
            {
                "data": {
                    "title": "Artifact 1",
                    "artifact_type": "android-apk",
                    "slug": "slug1",
                    "is_public_page_enabled": true,
                    "public_install_page_url": "https://bitrise.io/artifact/slug1/p"
                }
            }
        """.trimIndent()

        val repository = createRepository(
            responses = mapOf(
                "artifacts" to listContent,
                "artifacts/slug1" to detailContent
            )
        )

        val result = repository.getArtifacts("app", "build")

        assertEquals(1, result.size)
        assertEquals("Artifact 1", result[0].title)
        assertEquals("android-apk", result[0].artifactType)
        assertTrue(result[0].isPublicPageEnabled())
        assertEquals("https://bitrise.io/artifact/slug1/p", result[0].publicInstallPageUrl)
    }

    @Test
    fun `getArtifacts returns empty list on list error`() = runTest {
        val repository = createRepository(defaultStatus = HttpStatusCode.InternalServerError)

        val result = repository.getArtifacts("app", "build")

        assertTrue(result.isEmpty())
    }

    @Test
    fun `getArtifacts returns artifacts even if detail fails`() = runTest {
        val listContent = """
            {
                "data": [
                    {
                        "title": "Artifact 1",
                        "artifact_type": "android-apk",
                        "slug": "slug1",
                        "is_public_page_enabled": true
                    }
                ]
            }
        """.trimIndent()

        val repository = createRepository(
            responses = mapOf("artifacts" to listContent),
            defaultStatus = HttpStatusCode.NotFound
        )

        val result = repository.getArtifacts("app", "build")

        assertEquals(1, result.size)
        assertEquals("Artifact 1", result[0].title)
        assertEquals("", result[0].publicInstallPageUrl)
    }
}
