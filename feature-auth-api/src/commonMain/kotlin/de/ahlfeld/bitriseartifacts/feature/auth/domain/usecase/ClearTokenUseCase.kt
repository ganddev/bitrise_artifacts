package de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase

fun interface ClearTokenUseCase {
    suspend operator fun invoke()
}
