package de.ahlfeld.bitriseartifacts.builds.presentation

sealed interface BuildsUiEvent {
    data object OnBackClicked : BuildsUiEvent
}
