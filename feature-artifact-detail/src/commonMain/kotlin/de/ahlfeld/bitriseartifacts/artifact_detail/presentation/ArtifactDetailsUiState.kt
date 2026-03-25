package de.ahlfeld.bitriseartifacts.artifact_detail.presentation

import de.ahlfeld.bitriseartifacts.artifact_detail.domain.model.ArtifactDetails

internal sealed interface ArtifactDetailsUiState {
    data object Hidden : ArtifactDetailsUiState
    data class Visible(val artifacts: List<ArtifactDetails>) : ArtifactDetailsUiState
}