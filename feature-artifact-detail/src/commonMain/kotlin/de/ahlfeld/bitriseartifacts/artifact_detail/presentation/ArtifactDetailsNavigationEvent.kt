package de.ahlfeld.bitriseartifacts.artifact_detail.presentation

internal sealed interface ArtifactDetailsNavigationEvent {

    data object Back : ArtifactDetailsNavigationEvent
    data class OpenUrl(val url: String) : ArtifactDetailsNavigationEvent
}