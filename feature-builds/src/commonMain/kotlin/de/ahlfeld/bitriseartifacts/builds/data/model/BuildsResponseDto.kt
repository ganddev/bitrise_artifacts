package de.ahlfeld.bitriseartifacts.builds.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
internal data class BuildsResponseDto(
    @SerialName("data") val data: List<BuildDto>,
    @SerialName("paging") val paging: PagingDto
)

@Serializable
internal data class BuildDto(
    @SerialName("build_number") val buildNumber: Int,
    @SerialName("branch") val branch: String,
    @SerialName("triggered_at") val triggeredAt: String,
    @SerialName("finished_at") val finishedAt: String? = null,
    @SerialName("commit_hash") val commitHash: String? = null,
    @SerialName("slug") val slug: String,
    @SerialName("status") val status: Int
)

@Serializable
internal data class PagingDto(
    @SerialName("total_item_count") val totalItemCount: Int,
    @SerialName("page_item_limit") val pageItemLimit: Int,
    @SerialName("next") val next: String? = null
)
