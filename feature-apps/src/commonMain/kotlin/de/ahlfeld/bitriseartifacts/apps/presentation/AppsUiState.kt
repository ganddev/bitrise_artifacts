package de.ahlfeld.bitriseartifacts.apps.presentation

sealed interface AppsUiState {
    data object Loading : AppsUiState
    data class Content(
        val apps: List<AppItem>,
        val selectedAppSlug: String? = null
    ) : AppsUiState

    data class Error(val message: String) : AppsUiState
}