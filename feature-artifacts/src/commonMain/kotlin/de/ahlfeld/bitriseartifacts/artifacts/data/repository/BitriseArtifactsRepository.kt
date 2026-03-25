package de.ahlfeld.bitriseartifacts.artifacts.data.repository

import de.ahlfeld.bitriseartifacts.artifacts.api.model.Artifact
import de.ahlfeld.bitriseartifacts.artifacts.domain.repository.ArtifactsRepository
import de.ahlfeld.bitriseartifacts.artifacts.data.model.ArtifactsResponseDto
import de.ahlfeld.bitriseartifacts.artifacts.data.model.ArtifactDetailResponseDto
import de.ahlfeld.bitriseartifacts.artifacts.domain.NoArtifactFoundException
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.coroutineScope

internal class BitriseArtifactsRepository(
    private val httpClient: HttpClient
) : ArtifactsRepository {
    override suspend fun getArtifacts(appSlug: String, buildSlug: String): List<Artifact> =
        coroutineScope {
            val response = httpClient.get("apps/$appSlug/builds/$buildSlug/artifacts")
            if (response.status != HttpStatusCode.OK) {
                return@coroutineScope emptyList()
            }

            val artifactDtos = response.body<ArtifactsResponseDto>().data
            artifactDtos.map { dto ->
                async {
                    val detailResponse =
                        httpClient.get("apps/$appSlug/builds/$buildSlug/artifacts/${dto.slug}")
                    val detailDto = if (detailResponse.status == HttpStatusCode.OK) {
                        detailResponse.body<ArtifactDetailResponseDto>().data
                    } else {
                        null
                    }
                    Artifact(
                        slug = dto.slug,
                        title = dto.title,
                        artifactType = dto.artifactType,
                        publicInstallPageUrl = detailDto?.publicInstallPageUrl.orEmpty()
                    )
                }
            }.awaitAll()
        }
}
