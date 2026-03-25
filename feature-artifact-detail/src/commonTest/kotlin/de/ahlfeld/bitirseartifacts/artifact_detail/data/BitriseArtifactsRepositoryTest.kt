package de.ahlfeld.bitirseartifacts.artifact_detail.data

import de.ahlfeld.bitriseartifacts.artifact_detail.data.BitriseArtifactsRepository
import de.ahlfeld.bitriseartifacts.artifact_detail.domain.model.ArtifactDetails
import de.ahlfeld.bitriseartifacts.artifact_detail.domain.repository.ArtifactsRepository
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

class BitriseArtifactsRepositoryTest {

    private fun createMockHttpClient(
        content: String,
        status: HttpStatusCode = HttpStatusCode.OK
    ): HttpClient {
        val mockEngine = MockEngine {
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

    private lateinit var repository: ArtifactsRepository

    @Test
    fun `getArtifactDetails returns ArtifactDetails when status is OK`() = runTest {
        val json = readResource("ArtifactResponse.json")
        
        repository = BitriseArtifactsRepository(
            createMockHttpClient(
                content = json,
                status = HttpStatusCode.OK
            )
        )

        val result = repository.getArtifactDetails(
            appSlug = "app-slug",
            artifactSlug = "artifact-slug",
            buildSlug = "build-slug"
        )

        assertEquals(
            expected = ArtifactDetails(
                appName = "string",
                versionCode = "string",
                title = "string",
                publicUrl = "string"
            ), actual = result
        )
    }
}
