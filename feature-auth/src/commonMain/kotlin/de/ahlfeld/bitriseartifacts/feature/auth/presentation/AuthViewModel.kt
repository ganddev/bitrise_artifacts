package de.ahlfeld.bitriseartifacts.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase.SaveTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class AuthViewModel(
    private val saveTokenUseCase: SaveTokenUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun onTokenChanged(token: String) {
        _uiState.value = _uiState.value.copy(token = token)
    }

    fun onSaveToken() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            saveTokenUseCase(_uiState.value.token)
            _uiState.value = _uiState.value.copy(isLoading = false, isSaved = true)
        }
    }
}
