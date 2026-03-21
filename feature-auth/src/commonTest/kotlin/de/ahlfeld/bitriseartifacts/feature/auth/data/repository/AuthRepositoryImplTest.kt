package de.ahlfeld.bitriseartifacts.feature.auth.data.repository

import de.ahlfeld.bitriseartifacts.feature.auth.testdata.DataStoreFake
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {

    private val dataStore = DataStoreFake()
    private val repository = AuthRepositoryImpl(dataStore)
    private lateinit var testScope: TestScope

    @BeforeTest
    fun setup() {
        testScope = TestScope(UnconfinedTestDispatcher() + Job())
    }

    @AfterTest
    fun tearDown() {
        testScope.cancel()
    }

    @Test
    fun `getToken should return empty string initially`() = runTest {
        val token = repository.getToken().first()
        assertEquals("", token)
    }

    @Test
    fun `saveToken should save token to dataStore`() = runTest {
        val token = "test-token"
        repository.saveToken(token)

        val savedToken = repository.getToken().first()

        assertEquals(token, savedToken)
    }

    @Test
    fun `clearToken should remove token from dataStore`() = runTest {
        val storedToken = mutableListOf<String>()
        val job = launch(UnconfinedTestDispatcher()) {
            repository.getToken().collectLatest {
                storedToken.add(it)
            }
        }

        repository.saveToken("some-token")
        repository.clearToken()

        // Initial empty string, then "some-token", then "" after clear
        assertEquals(3, storedToken.size)
        assertEquals("", storedToken[0])
        assertEquals("some-token", storedToken[1])
        assertEquals("", storedToken[2])

        job.cancel()
    }
}
