package de.ahlfeld.bitriseartifacts.artifact_detail.presentation

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.viewModelScope
import de.ahlfeld.bitriseartifacts.artifact_detail.domain.usecase.GetArtifactDetailsUseCase
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

internal class ArtifactDetailsViewModelImpl(
    savedStateHandle: SavedStateHandle,
    private val getArtifactDetails: GetArtifactDetailsUseCase,
) : ArtifactDetailsViewModel() {

    private val appSlug: String = checkNotNull(savedStateHandle["appSlug"])
    private val artifactSlugs: List<String> =
        savedStateHandle.get<Array<String>>("artifactSlugs")?.toList()
            ?: savedStateHandle.get<List<String>>("artifactSlugs")
            ?: emptyList()

    private val buildSlug: String = checkNotNull(savedStateHandle["buildSlug"])

    private val _uiState = MutableStateFlow<ArtifactDetailsUiState>(ArtifactDetailsUiState.Hidden)
    override val uiState: StateFlow<ArtifactDetailsUiState> = _uiState.onStart {
        loadArtifactDetails(
            appSlug = appSlug,
            artifactSlugs = artifactSlugs,
            buildSlug = buildSlug
        )
    }.stateIn(viewModelScope, SharingStarted.WhileSubscribed(5_000), ArtifactDetailsUiState.Hidden)

    private val _navigationEvents = MutableSharedFlow<ArtifactDetailsNavigationEvent>()
    override val navigationEvents: SharedFlow<ArtifactDetailsNavigationEvent> = _navigationEvents

    override fun handleUiEvent(event: ArtifactDetailsUiEvent) {
        when (event) {
            ArtifactDetailsUiEvent.OnDismissed -> {
                _uiState.value = ArtifactDetailsUiState.Hidden
                viewModelScope.launch {
                    _navigationEvents.emit(ArtifactDetailsNavigationEvent.Back)
                }
            }

            is ArtifactDetailsUiEvent.OnArtifactClicked -> {
                if (event.item.publicUrl.isNotEmpty()) {
                    viewModelScope.launch {
                        _navigationEvents.emit(ArtifactDetailsNavigationEvent.OpenUrl(event.item.publicUrl))
                    }
                }
            }
        }
    }

    private fun loadArtifactDetails(
        appSlug: String,
        artifactSlugs: List<String>,
        buildSlug: String
    ) {
        viewModelScope.launch {
            val artifacts = artifactSlugs.map { artifactSlug ->
                async {
                    getArtifactDetails(
                        appSlug = appSlug,
                        artifactSlug = artifactSlug,
                        buildSlug = buildSlug
                    )
                }
            }.awaitAll()
                .mapNotNull { it.getOrNull() }

            _uiState.value = ArtifactDetailsUiState.Visible(artifacts)
        }
    }
}
