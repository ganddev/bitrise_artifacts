package de.ahlfeld.bitriseartifacts.apps.data.model

import kotlinx.serialization.SerialName
import kotlinx.serialization.Serializable

@Serializable
data class AppsResponseDto(
    @SerialName("data") val data: List<AppDto>,
    @SerialName("paging") val paging: PagingDto
)

@Serializable
data class AppDto(
    @SerialName("slug") val slug: String,
    @SerialName("title") val title: String,
    @SerialName("owner") val owner: OwnerDto,
    @SerialName("avatar_url") val avatarUrl: String? = null
)

@Serializable
data class OwnerDto(
    @SerialName("account_type") val accountType: String? = null,
    @SerialName("name") val name: String,
    @SerialName("slug") val slug: String? = null
)

@Serializable
data class PagingDto(
    @SerialName("total_item_count") val totalItemCount: Int,
    @SerialName("page_item_limit") val pageItemLimit: Int,
    @SerialName("next") val next: String? = null
)
