package de.ahlfeld.bitriseartifacts.apps.presentation

import androidx.lifecycle.SavedStateHandle
import de.ahlfeld.bitriseartifacts.apps.domain.model.App
import de.ahlfeld.bitriseartifacts.apps.domain.usecase.GetAppsUseCase
import de.ahlfeld.bitriseartifacts.apps.testdata.AppsRepositoryFake
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

    private val appsRepositoryFake = AppsRepositoryFake()
    private val viewModel = AppsViewModelImpl(
        getAppsUseCase = GetAppsUseCase(appsRepositoryFake),
        saveStateHandle = SavedStateHandle()
    )

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `initial state is Loading`() = runTest {
        val useCase = GetAppsUseCase(appsRepositoryFake)

        val viewModel = AppsViewModelImpl(getAppsUseCase = useCase, SavedStateHandle())

        assertEquals(AppsUiState.Loading, viewModel.uiState.value)
    }

    @Test
    fun `loadApps success updates state to Content`() = runTest {
        val apps = listOf(
            App("slug", "title", "owner", "avatar")
        )
        appsRepositoryFake.result = apps

        var actualUiState: AppsUiState? = null
        val job = launch {
            viewModel.uiState.collect {
                actualUiState = it
            }
        }

        advanceUntilIdle()

        assertEquals(
            AppsUiState.Content(
                apps = listOf(
                    AppItem("slug", "avatar", "owner", "title")
                ),
                selectedAppSlug = ""
            ), actualUiState
        )

        job.cancel()
    }

    @Test
    fun `loadApps failure updates state to Error`() = runTest {
        appsRepositoryFake.exception = Exception("Network error")

        var actualUiState: AppsUiState? = null
        val job = launch {
            viewModel.uiState.collect {
                actualUiState = it
            }
        }

        advanceUntilIdle()

        assertEquals(
            AppsUiState.Error("Network error"),
            actualUiState
        )

        job.cancel()
    }

    @Test
    fun `initial state with saved selected slug`() = runTest {
        val apps = listOf(App("slug", "title", "owner", "avatar"))
        appsRepositoryFake.result = apps
        val savedStateHandle = SavedStateHandle(mapOf("selected_app_slug" to "slug"))
        val viewModelWithSavedState = AppsViewModelImpl(
            getAppsUseCase = GetAppsUseCase(appsRepositoryFake),
            saveStateHandle = savedStateHandle
        )

        var actualUiState: AppsUiState? = null
        val job = launch {
            viewModelWithSavedState.uiState.collect {
                actualUiState = it
            }
        }

        advanceUntilIdle()

        assertEquals(
            AppsUiState.Content(
                apps = listOf(AppItem("slug", "avatar", "owner", "title")),
                selectedAppSlug = "slug"
            ),
            actualUiState
        )

        job.cancel()
    }

    @Test
    fun `handle OnAppItemClicked updates state and emits navigation event`() = runTest {
        val apps = listOf(App("slug", "title", "owner", "avatar"))
        appsRepositoryFake.result = apps
        val item = AppItem("slug", "avatar", "owner", "title")

        var lastNavigationEvent: AppsUiEvent? = null
        val navJob = launch {
            viewModel.navigationEvents.collect {
                lastNavigationEvent = it
            }
        }

        var lastUiState: AppsUiState? = null
        val stateJob = launch {
            viewModel.uiState.collect {
                lastUiState = it
            }
        }

        advanceUntilIdle()

        viewModel.handleUiEvent(AppsUiEvent.OnAppItemClicked(item))

        advanceUntilIdle()

        assertEquals(AppsUiEvent.OnAppItemClicked(item), lastNavigationEvent)
        assertEquals(
            AppsUiState.Content(
                apps = listOf(item),
                selectedAppSlug = "slug"
            ),
            lastUiState
        )

        navJob.cancel()
        stateJob.cancel()
    }
}