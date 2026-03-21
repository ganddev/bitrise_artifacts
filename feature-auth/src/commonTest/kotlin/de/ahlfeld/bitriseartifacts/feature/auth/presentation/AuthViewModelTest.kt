package de.ahlfeld.bitriseartifacts.feature.auth.presentation

import de.ahlfeld.bitriseartifacts.feature.auth.testdata.SaveTokenUseCaseFake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.StandardTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AuthViewModelTest {

    private val testDispatcher = StandardTestDispatcher()

    private val saveTokenUseCase = SaveTokenUseCaseFake()

    private val viewModel = AuthViewModel(saveTokenUseCase)

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun `initial state should be empty`() = runTest {
        val state = viewModel.uiState.value

        assertEquals("", state.token)
        assertEquals(false, state.isLoading)
        assertEquals(false, state.isSaved)
    }

    @Test
    fun `OnTokenChanged should update state`() = runTest {
        val newToken = "test-token"

        viewModel.handleUiEvent(LoginUiEvent.OnTokenChanged(newToken))

        assertEquals(newToken, viewModel.uiState.value.token)
    }

    @Test
    fun `OnLoginButtonClicked should call saveTokenUseCase and update state`() =
        runTest(testDispatcher) {
            val token = "my-token"
            val actualUiStates = mutableListOf<AuthUiState>()
            val job = launch {
                viewModel.uiState.collectLatest {
                    actualUiStates.add(it)
                }
            }
            advanceUntilIdle()

            viewModel.handleUiEvent(LoginUiEvent.OnTokenChanged(token))
            advanceUntilIdle()

            viewModel.handleUiEvent(LoginUiEvent.OnLoginButtonClicked)
            advanceUntilIdle()

            assertEquals(
                listOf(
                    AuthUiState(token = "", isLoading = false, isSaved = false, error = null),
                    AuthUiState(
                        token = "my-token",
                        isLoading = false,
                        isSaved = false,
                        error = null
                    ),
                    AuthUiState(
                        token = "my-token",
                        isLoading = false,
                        isSaved = true,
                        error = null
                    ),
                ), actualUiStates
            )

            job.cancel()
        }
}
