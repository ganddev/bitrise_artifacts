package de.ahlfeld.bitriseartifacts.apps.presentation

import androidx.lifecycle.viewModelScope
import de.ahlfeld.bitriseartifacts.apps.domain.usecase.GetAppsUseCase
import de.ahlfeld.bitriseartifacts.extensions.stateInWhileSubscribed
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart

class AppsViewModelImpl(
    private val getAppsUseCase: GetAppsUseCase
) : AppsViewModel() {
    override val uiState: StateFlow<AppsUiState> = flow {
        getAppsUseCase().onSuccess { apps ->
            emit(AppsUiState.Content(apps.map { app ->
                AppItem(
                    avatarUrl = app.avatarUrl ?: "",
                    ownerName = app.ownerName,
                    title = app.title
                )
            }))
        }.onFailure {
            emit(AppsUiState.Error(it.message ?: "Unknown error"))
        }
    }.onStart {
        emit(AppsUiState.Loading)
    }.stateInWhileSubscribed(
        scope = viewModelScope,
        initialValue = AppsUiState.Loading
    )

    override fun handleUiEvent(event: AppsUiEvent) {
        // Handle events
    }
}
