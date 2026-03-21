package de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase

import de.ahlfeld.bitriseartifacts.feature.auth.testdata.AuthRepositoryFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class ClearTokenUseCaseImplTest {
    private val repository = AuthRepositoryFake()

    private val clearTokenUseCase = ClearTokenUseCaseImpl(repository)

    @Test
    fun `invoke should call clearToken on repository`() = runTest {
        repository.saveToken("test-token")

        clearTokenUseCase()
        advanceUntilIdle()

        assertEquals("", repository.getToken().first())
    }
}
