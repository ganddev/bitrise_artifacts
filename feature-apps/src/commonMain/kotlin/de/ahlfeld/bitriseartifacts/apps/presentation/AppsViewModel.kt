package de.ahlfeld.bitriseartifacts.apps.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow

abstract class AppsViewModel : ViewModel() {
    abstract val uiState: StateFlow<AppsUiState>
    abstract val navigationEvents: SharedFlow<AppsUiEvent>

    abstract fun handleUiEvent(event: AppsUiEvent)
}
