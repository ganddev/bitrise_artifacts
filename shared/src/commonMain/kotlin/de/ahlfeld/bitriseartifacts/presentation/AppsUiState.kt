package de.ahlfeld.bitriseartifacts.presentation

sealed interface AppsUiState {
    data object Loading : AppsUiState
    data class Content(
        val apps : List<AppItem>
    )
}


data class AppItem(
    val avatarUrl : String,
    val ownerName: String,
    val title : String,
)