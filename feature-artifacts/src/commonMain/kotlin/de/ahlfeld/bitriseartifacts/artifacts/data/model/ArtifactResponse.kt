package de.ahlfeld.bitriseartifacts.artifacts.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class ArtifactsResponseDto(
    @SerialName("data") val data: List<ArtifactDto>
)

@Serializable
internal data class ArtifactDto(
    @SerialName("slug") val slug: String,
    @SerialName("title") val title: String,
    @SerialName("artifact_type") val artifactType: String? = null,
    @SerialName("is_public_page_enabled") val isPublicPageEnabled: Boolean
)

@Serializable
internal data class ArtifactDetailResponseDto(
    @SerialName("data") val data: ArtifactDetailDto
)

@Serializable
internal data class ArtifactDetailDto(
    @SerialName("slug") val slug: String,
    @SerialName("title") val title: String,
    @SerialName("artifact_type") val artifactType: String? = null,
    @SerialName("is_public_page_enabled") val isPublicPageEnabled: Boolean,
    @SerialName("public_install_page_url") val publicInstallPageUrl: String? = null
)
