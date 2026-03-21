package de.ahlfeld.bitriseartifacts.feature.auth.presentation

data class AuthUiState(
    val token: String = "",
    val isLoading: Boolean = false,
    val isSaved: Boolean = false,
    val error: String? = null
)
