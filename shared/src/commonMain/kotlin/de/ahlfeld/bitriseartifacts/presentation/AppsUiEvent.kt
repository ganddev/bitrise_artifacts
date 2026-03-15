package de.ahlfeld.bitriseartifacts.presentation

sealed interface AppsUiEvent {
    data class OnAppItemClicked(
        val item: AppItem
    ) : AppsUiEvent
}