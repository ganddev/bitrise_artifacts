package de.ahlfeld.bitriseartifacts.apps.presentation

import androidx.compose.ui.tooling.preview.PreviewParameterProvider

class AppsUiStateProvider : PreviewParameterProvider<AppsUiState> {
    override val values: Sequence<AppsUiState>
        get() = sequenceOf(
            AppsUiState.Error(
                message = "Error message"
            ),
            AppsUiState.Content(
                apps = emptyList()
            ),
            AppsUiState.Content(
                apps = listOf(
                    AppItem(
                        avatarUrl = "https://example.com/avatar1.png",
                        ownerName = "Owner Name 1",
                        title = "App Title 1"
                    ),
                    AppItem(
                        avatarUrl = "https://example.com/avatar2.png",
                        ownerName = "Owner Name 2",
                        title = "App Title 2"
                    ),
                )
            ),
            AppsUiState.Loading
        )

}
