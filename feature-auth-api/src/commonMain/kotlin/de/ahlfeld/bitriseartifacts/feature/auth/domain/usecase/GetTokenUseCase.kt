package de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase

import kotlinx.coroutines.flow.Flow

fun interface GetTokenUseCase {
    operator fun invoke(): Flow<String>
}
