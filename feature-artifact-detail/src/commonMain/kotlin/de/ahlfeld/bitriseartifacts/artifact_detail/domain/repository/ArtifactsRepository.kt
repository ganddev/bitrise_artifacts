package de.ahlfeld.bitriseartifacts.artifact_detail.domain.repository

import de.ahlfeld.bitriseartifacts.artifact_detail.domain.model.ArtifactDetails

internal interface ArtifactsRepository {

    suspend fun getArtifactDetails(appSlug: String, artifactSlug: String, buildSlug: String): ArtifactDetails
}