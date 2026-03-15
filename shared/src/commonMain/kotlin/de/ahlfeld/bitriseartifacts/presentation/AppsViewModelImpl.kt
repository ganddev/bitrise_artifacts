package de.ahlfeld.bitriseartifacts.presentation

import androidx.lifecycle.viewModelScope
import de.ahlfeld.bitriseartifacts.extensions.stateInWhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow


class AppsViewModelImpl : AppsViewModel() {
    override val uiState: StateFlow<AppsUiState> = flow {
        emit(AppsUiState.Loading)
    }.stateInWhileSubscribed(
        scope = viewModelScope,
        initialValue = AppsUiState.Loading
    )

    override fun handleUiEvent(event: AppsUiEvent) {
        TODO("Not yet implemented")
    }
}