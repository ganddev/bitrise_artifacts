package de.ahlfeld.bitriseartifacts.artifacts.domain.repository

import de.ahlfeld.bitriseartifacts.artifacts.api.model.Artifact

internal interface ArtifactsRepository {
    suspend fun getArtifacts(appSlug: String, buildSlug: String): List<Artifact>
}