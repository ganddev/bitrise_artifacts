package de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase

import de.ahlfeld.bitriseartifacts.feature.auth.domain.repository.AuthRepository

class SaveTokenUseCase(private val repository: AuthRepository) {
    suspend operator fun invoke(token: String) = repository.saveToken(token)
}
