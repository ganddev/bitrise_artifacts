package de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase

import de.ahlfeld.bitriseartifacts.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

internal class GetTokenUseCaseImpl(
    private val repository: AuthRepository
) : GetTokenUseCase {
    override operator fun invoke(): Flow<String> = repository.getToken()
}
