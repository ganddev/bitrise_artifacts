package de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase

import de.ahlfeld.bitriseartifacts.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow

class GetTokenUseCase(private val repository: AuthRepository) {
    operator fun invoke(): Flow<String?> = repository.getToken()
}
