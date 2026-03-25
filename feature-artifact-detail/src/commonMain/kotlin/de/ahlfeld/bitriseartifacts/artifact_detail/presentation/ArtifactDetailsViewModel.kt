package de.ahlfeld.bitriseartifacts.artifact_detail.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

internal abstract class ArtifactDetailsViewModel : ViewModel() {
    abstract val navigationEvents: SharedFlow<ArtifactDetailsNavigationEvent>
    abstract val uiState: StateFlow<ArtifactDetailsUiState>
    abstract fun handleUiEvent(event: ArtifactDetailsUiEvent)
}

