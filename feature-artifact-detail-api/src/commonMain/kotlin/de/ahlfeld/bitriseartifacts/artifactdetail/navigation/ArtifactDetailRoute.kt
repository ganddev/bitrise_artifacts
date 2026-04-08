package de.ahlfeld.bitriseartifacts.artifactdetail.navigation

import kotlinx.serialization.Serializable

@Serializable
data class ArtifactDetailRoute(
    val appSlug: String,
    val artifactSlugs: List<String>,
    val buildSlug: String
)