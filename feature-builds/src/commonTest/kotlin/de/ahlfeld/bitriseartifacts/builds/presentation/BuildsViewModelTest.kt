package de.ahlfeld.bitriseartifacts.builds.presentation

import androidx.lifecycle.SavedStateHandle
import de.ahlfeld.bitriseartifacts.builds.domain.model.Build
import de.ahlfeld.bitriseartifacts.builds.domain.model.BuildsPage
import de.ahlfeld.bitriseartifacts.builds.domain.usecase.GetBuildsUseCase
import de.ahlfeld.bitriseartifacts.builds.testdata.BuildsRepositoryFake
import de.ahlfeld.bitriseartifacts.builds.testdata.GetArtifactSlugsUseCaseFake
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runCurrent
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertTrue

@OptIn(ExperimentalCoroutinesApi::class)
class BuildsViewModelTest {

    private val testDispatcher = UnconfinedTestDispatcher()

    private val buildsRepositoryFake = BuildsRepositoryFake()
    private val getArtifactSlugsUseCaseFake = GetArtifactSlugsUseCaseFake()

    private val savedStateHandle = SavedStateHandle().apply {
        set("appSlug", "test-app-slug")
    }

    private val viewModel = BuildsViewModelImpl(
        savedStateHandle,
        GetBuildsUseCase(buildsRepositoryFake),
        getArtifactSlugs = getArtifactSlugsUseCaseFake,
    )

    @BeforeTest
    fun setup() {
        Dispatchers.setMain(testDispatcher)
    }

    @Test
    fun `loadBuilds success updates state to Content and sorts by date desc`() = runTest {
        val builds = listOf(
            Build(1, "main", "2023-01-01T10:00:00Z", null, "hash1", "slug1", 1),
            Build(2, "develop", "2023-01-02T10:00:00Z", null, "hash2", "slug2", 1)
        )
        buildsRepositoryFake.result = BuildsPage(builds, null)
        getArtifactSlugsUseCaseFake.result = listOf("slug1", "slug2")


        val states = mutableListOf<BuildsUiState>()
        val job = launch(testDispatcher) {
            viewModel.uiState.collect { states.add(it) }
        }

        runCurrent()

        assertTrue(states.isNotEmpty(), "States should not be empty")
        val state = states.last()
        assertTrue(state is BuildsUiState.Content, "Expected Content state but was $state")
        assertEquals(2, state.builds.size)
        // Check order: Most recent first (build 2 has later date)
        assertEquals(2, state.builds[0].buildNumber)
        assertEquals(1, state.builds[1].buildNumber)
        job.cancel()
    }

    @Test
    fun `loadBuilds success with empty list updates state to Content with empty list`() = runTest {
        val builds = emptyList<Build>()
        buildsRepositoryFake.result = BuildsPage(builds, null)

        val states = mutableListOf<BuildsUiState>()
        val job = launch(testDispatcher) {
            viewModel.uiState.collect { states.add(it) }
        }

        runCurrent()

        assertTrue(states.isNotEmpty(), "States should not be empty")
        val state = states.last()
        assertTrue(state is BuildsUiState.Content, "Expected Content state but was $state")
        assertTrue(state.builds.isEmpty())
        job.cancel()
    }

    @Test
    fun `loadBuilds failure updates state to Error`() = runTest {
        val repository = BuildsRepositoryFake().apply {
            exception = Exception("Network error")

        }
        val useCase = GetBuildsUseCase(repository)

        val viewModel = BuildsViewModelImpl(
            savedStateHandle,
            getBuildsUseCase = useCase,
            getArtifactSlugs = getArtifactSlugsUseCaseFake
        )

        val states = mutableListOf<BuildsUiState>()
        val job = launch(testDispatcher) {
            viewModel.uiState.collect { states.add(it) }
        }

        runCurrent()

        assertTrue(states.isNotEmpty(), "States should not be empty")
        val state = states.last()
        assertTrue(state is BuildsUiState.Error, "Expected Error state but was $state")
        assertEquals("Network error", state.message)
        job.cancel()
    }

    @Test
    fun `loadBuilds failure with null message updates state to Error with default message`() =
        runTest {
            buildsRepositoryFake.exception = Exception()

            val states = mutableListOf<BuildsUiState>()
            val job = launch(testDispatcher) {
                viewModel.uiState.collect { states.add(it) }
            }

            runCurrent()

            assertTrue(states.isNotEmpty(), "States should not be empty")
            val state = states.last()
            assertTrue(state is BuildsUiState.Error, "Expected Error state but was $state")
            assertEquals("Unknown error", state.message)
            job.cancel()
        }

    @Test
    fun `handleUiEvent OnBackClicked emits Back navigation event`() = runTest {
        buildsRepositoryFake.result = BuildsPage(emptyList(), null)
        val events = mutableListOf<BuildsNavigationEvent>()
        val job = launch(testDispatcher) {
            viewModel.navigationEvents.collect { events.add(it) }
        }

        viewModel.handleUiEvent(BuildsUiEvent.OnBackClicked)
        runCurrent()

        assertEquals(1, events.size)
        assertEquals(BuildsNavigationEvent.Back, events[0])
        job.cancel()
    }
}
