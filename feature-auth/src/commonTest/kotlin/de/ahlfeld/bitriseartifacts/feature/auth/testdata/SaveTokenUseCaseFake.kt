package de.ahlfeld.bitriseartifacts.feature.auth.testdata

import de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase.SaveTokenUseCase
import de.ahlfeld.bitriseartifacts.feature.auth.presentation.AuthViewModelTest

class SaveTokenUseCaseFake : SaveTokenUseCase {
    var passedToken: String? = null
        private set

    override suspend fun invoke(token: String) {
        passedToken = token
    }
}