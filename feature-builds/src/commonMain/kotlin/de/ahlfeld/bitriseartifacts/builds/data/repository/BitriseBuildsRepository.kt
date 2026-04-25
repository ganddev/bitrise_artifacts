package de.ahlfeld.bitriseartifacts.builds.data.repository

import de.ahlfeld.bitriseartifacts.builds.data.model.BuildsResponseDto
import de.ahlfeld.bitriseartifacts.builds.domain.model.Build
import de.ahlfeld.bitriseartifacts.builds.domain.model.BuildsPage
import de.ahlfeld.bitriseartifacts.builds.domain.repository.BuildsRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.request.parameter
import io.ktor.http.HttpStatusCode

internal class BitriseBuildsRepository(
    private val httpClient: HttpClient
) : BuildsRepository {
    override suspend fun getBuilds(appSlug: String, next: String?): BuildsPage {
        val response = httpClient.get("apps/$appSlug/builds") {
            parameter("next", next)
        }
        return when (response.status) {
            HttpStatusCode.OK -> {
                val dto = response.body<BuildsResponseDto>()
                BuildsPage(
                    builds = dto.data.map { buildDto ->
                        Build(
                            buildNumber = buildDto.buildNumber,
                            branch = buildDto.branch,
                            triggeredAt = buildDto.triggeredAt,
                            finishedAt = buildDto.finishedAt,
                            commitHash = buildDto.commitHash,
                            slug = buildDto.slug,
                            status = buildDto.status
                        )
                    },
                    next = dto.paging.next
                )
            }

            else -> BuildsPage(emptyList(), null)
        }
    }
}
