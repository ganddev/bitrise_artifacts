package de.ahlfeld.bitriseartifacts.feature.auth.presentation

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase.SaveTokenUseCase
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class AuthViewModel(
    private val saveTokenUseCase: SaveTokenUseCase
) : ViewModel() {

    private val _uiState = MutableStateFlow(AuthUiState())
    val uiState = _uiState.asStateFlow()

    fun handleUiEvent(event: LoginUiEvent) {
        when (event) {
            is LoginUiEvent.OnLoginButtonClicked -> onSaveToken()
            is LoginUiEvent.OnTokenChanged -> _uiState.update {
                it.copy(token = event.token)
            }
        }
    }

    private fun onSaveToken() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(isLoading = true)
            saveTokenUseCase(_uiState.value.token)
            _uiState.value = _uiState.value.copy(isLoading = false, isSaved = true)
        }
    }
}
