package de.ahlfeld.bitriseartifacts.apps.data.repository

import de.ahlfeld.bitriseartifacts.apps.data.model.AppsResponseDto
import de.ahlfeld.bitriseartifacts.apps.domain.model.App
import de.ahlfeld.bitriseartifacts.apps.domain.repository.AppsRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

class BitriseAppsRepository(
    private val httpClient: HttpClient
) : AppsRepository {
    override suspend fun getApps(): List<App> {
        val response = httpClient.get("apps")
        return if (response.status == HttpStatusCode.OK) {
            response.body<AppsResponseDto>().data.map { dto ->
                App(
                    slug = dto.slug,
                    title = dto.title,
                    ownerName = dto.owner.name,
                    avatarUrl = dto.avatarUrl
                )
            }
        } else {
            throw Exception("Failed to fetch apps: ${response.status}")
        }
    }
}
