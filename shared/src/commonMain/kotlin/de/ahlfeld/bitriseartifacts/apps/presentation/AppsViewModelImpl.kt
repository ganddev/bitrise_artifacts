package de.ahlfeld.bitriseartifacts.apps.presentation

import androidx.lifecycle.viewModelScope
import de.ahlfeld.bitriseartifacts.apps.domain.usecase.GetAppsUseCase
import de.ahlfeld.bitriseartifacts.extensions.stateInWhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class AppsViewModelImpl constructor(
    private val getAppsUseCase: GetAppsUseCase
) : AppsViewModel() {
    override val uiState: StateFlow<AppsUiState> = flow {
        emit(AppsUiState.Loading)
    }.onStart {
        val result = getAppsUseCase()
    }.stateInWhileSubscribed(
        scope = viewModelScope,
        initialValue = AppsUiState.Loading
    )

    override fun handleUiEvent(event: AppsUiEvent) {
        TODO("Not yet implemented")
    }
}