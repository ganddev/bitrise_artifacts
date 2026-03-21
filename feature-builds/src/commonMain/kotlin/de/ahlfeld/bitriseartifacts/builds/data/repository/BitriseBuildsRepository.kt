package de.ahlfeld.bitriseartifacts.builds.data.repository

import de.ahlfeld.bitriseartifacts.builds.data.model.BuildsResponseDto
import de.ahlfeld.bitriseartifacts.builds.domain.model.Build
import de.ahlfeld.bitriseartifacts.builds.domain.repository.BuildsRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

internal class BitriseBuildsRepository(
    private val httpClient: HttpClient
) : BuildsRepository {
    override suspend fun getBuilds(appSlug: String): List<Build> {
        val response = httpClient.get("apps/$appSlug/builds")
        return when (response.status) {
            HttpStatusCode.OK -> {
                response.body<BuildsResponseDto>().data.map { dto ->
                    Build(
                        buildNumber = dto.buildNumber,
                        branch = dto.branch,
                        triggeredAt = dto.triggeredAt,
                        finishedAt = dto.finishedAt,
                        commitHash = dto.commitHash,
                        slug = dto.slug,
                        status = dto.status
                    )
                }
            }

            else -> emptyList()
        }
    }
}
