package de.ahlfeld.bitriseartifacts.builds.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class BuildsViewModel : ViewModel() {
    abstract val navigationEvents : SharedFlow<BuildsNavigationEvent>
    abstract val uiState: StateFlow<BuildsUiState>
    abstract fun handleUiEvent(event: BuildsUiEvent)
}
