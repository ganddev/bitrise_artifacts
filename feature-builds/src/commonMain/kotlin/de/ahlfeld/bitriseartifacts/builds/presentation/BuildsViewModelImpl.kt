package de.ahlfeld.bitriseartifacts.builds.presentation

import androidx.lifecycle.viewModelScope
import de.ahlfeld.bitriseartifacts.builds.domain.usecase.GetBuildsUseCase
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class BuildsViewModelImpl(
    private val appSlug: String,
    private val getBuildsUseCase: GetBuildsUseCase
) : BuildsViewModel() {

    private val _uiState = MutableStateFlow<BuildsUiState>(BuildsUiState.Loading)

    private val _navigationEvents = MutableSharedFlow<BuildsNavigationEvent>()
    override val navigationEvents: SharedFlow<BuildsNavigationEvent> = _navigationEvents
    override val uiState: StateFlow<BuildsUiState> = _uiState.onStart {
        loadBuilds()
    }.stateIn(
        scope = viewModelScope,
        started = SharingStarted.WhileSubscribed(5_000),
        initialValue = BuildsUiState.Loading
    )


    private fun loadBuilds() {
        viewModelScope.launch {
            _uiState.value = BuildsUiState.Loading
            getBuildsUseCase(appSlug)
                .onSuccess { builds ->
                    val items = builds
                        .sortedByDescending { it.triggeredAt }
                        .map {
                            BuildItem(
                                buildNumber = it.buildNumber,
                                branch = it.branch,
                                triggeredAt = it.triggeredAt,
                                finishedAt = it.finishedAt,
                                commitHash = it.commitHash,
                                status = it.status
                            )
                        }
                    _uiState.value = BuildsUiState.Content(items)
                }
                .onFailure {
                    _uiState.value = BuildsUiState.Error(it.message ?: "Unknown error")
                }
        }
    }

    override fun handleUiEvent(event: BuildsUiEvent) {
        when (event) {
            BuildsUiEvent.OnBackClicked ->
                viewModelScope.launch { _navigationEvents.emit(BuildsNavigationEvent.Back) }
        }
    }
}
