package de.ahlfeld.bitriseartifacts.apps.presentation

import de.ahlfeld.bitriseartifacts.apps.domain.model.App
import de.ahlfeld.bitriseartifacts.apps.domain.usecase.GetAppsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.advanceUntilIdle
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AppsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    @OptIn(ExperimentalCoroutinesApi::class)
    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `initial state is Loading`() = runTest {
        val useCase = GetAppsUseCase(FakeAppsRepository())

        val viewModel = AppsViewModelImpl(useCase)

        assertEquals(AppsUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `loadApps success updates state to Content`() = runTest {
        val apps = listOf(
            App("slug", "title", "owner", "avatar")
        )
        val useCase = GetAppsUseCase(FakeAppsRepository(apps))
        val viewModel = AppsViewModelImpl(useCase)

        var actualUiState: AppsUiState? = null
        val job = launch {
            viewModel.uiState.collect {
                actualUiState = it
            }
        }

        advanceUntilIdle()

        // Wait for the flow to emit the Content state
        // Since we use UnconfinedTestDispatcher and stateIn, it should be immediate if the flow completes

        assertEquals(
            AppsUiState.Content(
                apps = listOf(
                    element = AppItem("avatar", "owner", "title")
                )
            ), actualUiState
        )

        job.cancel()
    }
}

private class FakeAppsRepository(private val apps: List<App> = emptyList()) :
    de.ahlfeld.bitriseartifacts.apps.domain.repository.AppsRepository {
    override suspend fun getApps(): List<App> = apps
}
