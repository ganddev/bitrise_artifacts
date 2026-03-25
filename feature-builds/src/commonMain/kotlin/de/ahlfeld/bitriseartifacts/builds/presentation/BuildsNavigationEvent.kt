package de.ahlfeld.bitriseartifacts.builds.presentation

internal sealed interface BuildsNavigationEvent {
    data object Back : BuildsNavigationEvent

    data class ShowArtifactDetails(
        val appSlug: String,
        val artifactSlugs: List<String>,
        val buildSlug: String
    ) : BuildsNavigationEvent
}
