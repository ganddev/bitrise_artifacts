package de.ahlfeld.bitriseartifacts.presentation

import androidx.lifecycle.ViewModel
import kotlinx.coroutines.flow.StateFlow

abstract class AppsViewModel : ViewModel() {
    abstract val uiState : StateFlow<AppsUiState>

    abstract fun handleUiEvent(event : AppsUiEvent)
}