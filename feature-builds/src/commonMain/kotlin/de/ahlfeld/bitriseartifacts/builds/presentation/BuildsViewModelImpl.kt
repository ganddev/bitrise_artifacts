package de.ahlfeld.bitriseartifacts.builds.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import de.ahlfeld.bitriseartifacts.artifacts.api.usecase.GetArtifactSlugsUseCase
import de.ahlfeld.bitriseartifacts.builds.domain.usecase.GetBuildsUseCase
import kotlinx.coroutines.async
import kotlinx.coroutines.awaitAll
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharedFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

internal class BuildsViewModelImpl(
    savedStateHandle: SavedStateHandle,
    private val getBuildsUseCase: GetBuildsUseCase,
    private val getArtifactSlugs: GetArtifactSlugsUseCase,
) : BuildsViewModel() {

    private val appSlug : String = checkNotNull(savedStateHandle["appSlug"])

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
                        .map { build ->
                            async {
                                BuildItem(
                                    buildNumber = build.buildNumber,
                                    branch = build.branch,
                                    triggeredAt = build.triggeredAt,
                                    finishedAt = build.finishedAt,
                                    commitHash = build.commitHash,
                                    buildSlug = build.slug,
                                    artifactSlugs = getArtifactSlugs(appSlug, build.slug)
                                )
                            }
                        }.awaitAll()
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

            is BuildsUiEvent.OnBuildClicked -> {
                val currentState = _uiState.value
                if (currentState is BuildsUiState.Content) {
                    _uiState.value = currentState.copy(selectedBuildSlug = event.buildSlug)
                    viewModelScope.launch {
                        _navigationEvents.emit(
                            BuildsNavigationEvent.ShowArtifactDetails(
                                appSlug,
                                artifactSlugs = event.artifactSlugs,
                                buildSlug = event.buildSlug
                            )
                        )
                    }
                }
            }
        }
    }
}
