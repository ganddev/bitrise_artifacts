package de.ahlfeld.bitriseartifacts.apps.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import de.ahlfeld.bitriseartifacts.apps.domain.model.App
import de.ahlfeld.bitriseartifacts.apps.domain.usecase.GetAppsUseCase
import de.ahlfeld.bitriseartifacts.extensions.stateInWhileSubscribed
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.combine
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

private const val KEY_SELECTED_APP_SLUG = "selected_app_slug"

class AppsViewModelImpl(
    private val getAppsUseCase: GetAppsUseCase,
    private val saveStateHandle: SavedStateHandle
) : AppsViewModel() {

    private val _selectedAppSlug = saveStateHandle.getStateFlow(KEY_SELECTED_APP_SLUG, "")

    override val uiState: StateFlow<AppsUiState> = combine(
        flow {
            emit(getAppsUseCase())
        },
        _selectedAppSlug
    ) { result, selectedSlug ->
        createUiState(result, selectedSlug)
    }.onStart {
        emit(AppsUiState.Loading)
    }.stateInWhileSubscribed(
        scope = viewModelScope,
        initialValue = AppsUiState.Loading
    )

    private val _navigationEvents = MutableSharedFlow<AppsUiEvent>()

    override val navigationEvents: SharedFlow<AppsUiEvent> = _navigationEvents.asSharedFlow()
    override fun handleUiEvent(event: AppsUiEvent) {
        when (event) {
            is AppsUiEvent.OnAppItemClicked -> {
                saveStateHandle[KEY_SELECTED_APP_SLUG] = event.item.slug
                viewModelScope.launch {
                    _navigationEvents.emit(event)
                }
            }
        }
    }

    private fun createUiState(
        result: Result<List<App>>,
        selectedSlug: String?
    ): AppsUiState = if (result.isSuccess) {
        val apps = result.getOrThrow()
        AppsUiState.Content(
            apps = apps.map { app ->
                AppItem(
                    slug = app.slug,
                    avatarUrl = app.avatarUrl ?: "",
                    ownerName = app.ownerName,
                    title = app.title
                )
            },
            selectedAppSlug = selectedSlug
        )
    } else {
        AppsUiState.Error(result.exceptionOrNull()?.message ?: "Unknown error")
    }
}
