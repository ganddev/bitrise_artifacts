package de.ahlfeld.bitriseartifacts.artifact_detail.presentation

import de.ahlfeld.bitriseartifacts.artifact_detail.domain.model.ArtifactDetails

internal sealed interface ArtifactDetailsUiEvent {
    data object OnDismissed : ArtifactDetailsUiEvent
    data class OnArtifactClicked(val item: ArtifactDetails) : ArtifactDetailsUiEvent
}