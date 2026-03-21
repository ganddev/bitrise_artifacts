package de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase

import de.ahlfeld.bitriseartifacts.feature.auth.testdata.AuthRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class GetTokenUseCaseImplTest {

    private val repository = AuthRepositoryFake()
    private val getTokenUseCase = GetTokenUseCaseImpl(repository)

    @Test
    fun `invoke should return token from repository`() = runTest {
        val tokens = mutableListOf<String>()
        val job = launch {
            getTokenUseCase().collect { token ->
                tokens.add(token)
            }
        }

        repository.saveToken("test-token")
        advanceUntilIdle()

        assertEquals(listOf("test-token"), tokens)

        job.cancel()
    }
}
