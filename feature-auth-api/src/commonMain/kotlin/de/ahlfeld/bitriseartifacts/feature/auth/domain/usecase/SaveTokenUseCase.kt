package de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase

fun interface SaveTokenUseCase {
    suspend operator fun invoke(token: String)
}
