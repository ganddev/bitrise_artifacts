package de.ahlfeld.bitriseartifacts.artifact_detail.data

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ArtifactDetailResponseDto(
    @SerialName("data") val data: ArtifactDetailDto
)
@Serializable
internal data class ArtifactDetailDto(
    @SerialName("slug") val slug: String,
    @SerialName("title") val title: String,
    @SerialName("artifact_meta") val artifactMeta: ArtifactMetaDto?,
    @SerialName("public_install_page_url") val publicInstallPageUrl: String? = null
)

@Serializable
internal data class ArtifactMetaDto(
    @SerialName("app_info") val appInfoDto: AppInfoDto
)
@Serializable
internal data class AppInfoDto(
    @SerialName("app_name") val appName : String,
    @SerialName("package_name") val packageName : String,
    @SerialName("version_code") val versionCode : String,
    @SerialName("version_name") val versionName : String
)