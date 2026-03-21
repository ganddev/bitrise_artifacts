package de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase

import de.ahlfeld.bitriseartifacts.feature.auth.domain.repository.AuthRepository

internal class ClearTokenUseCaseImpl(
    private val repository: AuthRepository
) : ClearTokenUseCase {
    override suspend operator fun invoke() = repository.clearToken()
}
