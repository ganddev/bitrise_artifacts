package de.ahlfeld.bitriseartifacts.builds.presentation

sealed interface BuildsUiState {
    data object Loading : BuildsUiState
    data class Content(val builds: List<BuildItem>) : BuildsUiState
    data class Error(val message: String) : BuildsUiState
}

data class BuildItem(
    val buildNumber: Int,
    val branch: String,
    val triggeredAt: String,
    val finishedAt: String?,
    val commitHash: String?,
    val status: Int
)
