package de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase

import de.ahlfeld.bitriseartifacts.feature.auth.domain.repository.AuthRepository

internal class SaveTokenUseCaseImpl(
    private val repository: AuthRepository
) : SaveTokenUseCase {
    override suspend operator fun invoke(token: String) = repository.saveToken(token)
}
