package de.ahlfeld.bitriseartifacts.builds.presentation

sealed interface BuildsUiState {
    data object Loading : BuildsUiState
    data class Content(
        val builds: List<BuildItem>,
        val selectedBuildSlug: String? = null,
        val isLoadingMore: Boolean = false,
        val hasMore: Boolean = false
    ) : BuildsUiState
    data class Error(val message: String) : BuildsUiState
}

data class BuildItem(
    val buildNumber: Int,
    val branch: String,
    val triggeredAt: String,
    val finishedAt: String?,
    val commitHash: String?,
    val buildSlug: String,
    val artifactSlugs : List<String> = emptyList()
)
