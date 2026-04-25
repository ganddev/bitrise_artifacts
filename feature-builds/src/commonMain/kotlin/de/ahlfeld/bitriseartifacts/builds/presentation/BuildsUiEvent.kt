package de.ahlfeld.bitriseartifacts.builds.presentation

internal sealed interface BuildsUiEvent {
    data object OnBackClicked : BuildsUiEvent
    data class OnBuildClicked(val artifactSlugs: List<String>, val buildSlug: String) : BuildsUiEvent
    data object OnPageEnd : BuildsUiEvent
}
