package de.ahlfeld.bitriseartifacts.artifact_detail.data

import de.ahlfeld.bitriseartifacts.artifact_detail.domain.model.ArtifactDetails
import de.ahlfeld.bitriseartifacts.artifact_detail.domain.model.NoArtifactDetailsFoundException
import de.ahlfeld.bitriseartifacts.artifact_detail.domain.repository.ArtifactsRepository
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.http.HttpStatusCode

internal class BitriseArtifactsRepository(
    private val httpClient: HttpClient
) : ArtifactsRepository {

    override suspend fun getArtifactDetails(
        appSlug: String,
        artifactSlug: String,
        buildSlug: String
    ): ArtifactDetails {
        val response = httpClient.get("apps/$appSlug/builds/$buildSlug/artifacts/$artifactSlug")
        val artifactDto = if (response.status == HttpStatusCode.OK) {
            response.body<ArtifactDetailResponseDto>().data
        } else {
            throw NoArtifactDetailsFoundException(
                appSlug = appSlug,
                artifactSlug = artifactSlug,
                buildSlug = buildSlug
            )
        }
        if(artifactDto.artifactMeta == null) {
            throw NoArtifactDetailsFoundException(
                appSlug = appSlug,
                artifactSlug = artifactSlug,
                buildSlug = buildSlug
            )
        }
        return ArtifactDetails(
            appName = artifactDto.artifactMeta.appInfoDto.appName,
            versionCode = artifactDto.artifactMeta.appInfoDto.versionCode,
            title = artifactDto.title,
            publicUrl = artifactDto.publicInstallPageUrl.orEmpty()
        )
    }
}