package de.ahlfeld.bitriseartifacts.feature.auth.presentation

sealed interface LoginUiEvent {
    data object OnLoginButtonClicked : LoginUiEvent
    data class OnTokenChanged(val token: String) : LoginUiEvent

}
