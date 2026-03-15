package de.ahlfeld.bitriseartifacts.apps.presentation

sealed interface AppsUiState {
    data object Loading : AppsUiState
    data class Content(val apps: List<AppItem>) : AppsUiState
    data class Error(val message: String) : AppsUiState
}

data class AppItem(
    val avatarUrl : String,
    val ownerName: String,
    val title : String,
)