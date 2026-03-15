package de.ahlfeld.bitriseartifacts.apps.presentation

import de.ahlfeld.bitriseartifacts.apps.domain.model.App
import de.ahlfeld.bitriseartifacts.apps.domain.usecase.GetAppsUseCase
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

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
        
        // In a real TDD we might need to trigger loadApps if not in init, 
        // but let's assume it's in init or we call it.
        
        val expectedApps = listOf(
            AppItem("avatar", "owner", "title")
        )
        assertTrue(viewModel.uiState.value is AppsUiState.Content)
        assertEquals(expectedApps, (viewModel.uiState.value as AppsUiState.Content).apps)
    }
}

private class FakeAppsRepository(private val apps: List<App> = emptyList()) : de.ahlfeld.bitriseartifacts.apps.domain.repository.AppsRepository {
    override suspend fun getApps(): List<App> = apps
}
