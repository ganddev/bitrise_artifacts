package de.ahlfeld.bitriseartifacts.builds.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

internal class BuildsUiStatePreviewParameterProvider : PreviewParameterProvider<BuildsUiState> {
    override val values: Sequence<BuildsUiState>
        get() = sequenceOf(
            BuildsUiState.Loading,
            BuildsUiState.Content(
                builds = List(5) { index ->
                    BuildItem(
                        buildNumber = 120 + index,
                        branch = "main",
                        triggeredAt = "2023-10-27T10:00:00Z",
                        finishedAt = "2023-10-27T10:15:00Z",
                        commitHash = "abc12345678+$index",
                        buildSlug = "$index",
                        artifactSlugs = if (index % 2 == 0) listOf("apk-slug-$index") else emptyList()
                    )

                }
            ),
            BuildsUiState.Error("Failed to load builds")
        )
}
