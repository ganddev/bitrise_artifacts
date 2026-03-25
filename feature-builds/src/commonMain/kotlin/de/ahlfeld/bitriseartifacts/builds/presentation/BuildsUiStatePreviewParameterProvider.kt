package de.ahlfeld.bitriseartifacts.builds.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class BuildsUiStatePreviewParameterProvider : PreviewParameterProvider<BuildsUiState> {
    override val values: Sequence<BuildsUiState>
        get() = sequenceOf(
            BuildsUiState.Loading,
            BuildsUiState.Content(
                builds = listOf(
                    BuildItem(
                        buildNumber = 123,
                        branch = "main",
                        triggeredAt = "2023-10-27T10:00:00Z",
                        finishedAt = "2023-10-27T10:15:00Z",
                        commitHash = "abc123456789",
                        status = 1
                    ),
                    BuildItem(
                        buildNumber = 122,
                        branch = "feature/navigation",
                        triggeredAt = "2023-10-26T14:00:00Z",
                        finishedAt = null,
                        commitHash = "def987654321",
                        status = 0
                    )
                )
            ),
            BuildsUiState.Error("Failed to load builds")
        )
}
