package de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase

import de.ahlfeld.bitriseartifacts.feature.auth.testdata.AuthRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class SaveTokenUseCaseImplTest {
    private val repository = AuthRepositoryFake()

    private val saveTokenUseCase = SaveTokenUseCaseImpl(repository)

    @Test
    fun `invoke should call saveToken on repository`() = runTest {
        val token = "new-token"

        saveTokenUseCase(token)
        advanceUntilIdle()

        assertEquals(token, repository.getToken().first())
    }
}
