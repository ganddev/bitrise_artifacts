package de.ahlfeld.bitriseartifacts.apps.presentation

sealed interface AppsUiEvent {
    data class OnAppItemClicked(
        val item: AppItem
    ) : AppsUiEvent
}