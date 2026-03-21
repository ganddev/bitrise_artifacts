package de.ahlfeld.bitriseartifacts.feature.auth.data.repository

import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.PreferenceDataStoreFactory
import androidx.datastore.preferences.core.Preferences
import de.ahlfeld.bitriseartifacts.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.Job
import kotlinx.coroutines.cancel
import kotlinx.coroutines.flow.collectLatest
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.coroutines.test.TestScope
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.runTest
import okio.Path.Companion.toPath
import kotlin.random.Random
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class AuthRepositoryImplTest {

    private lateinit var dataStore: DataStore<Preferences>
    private lateinit var repository: AuthRepository
    private lateinit var testScope: TestScope

    @BeforeTest
    fun setup() {
        testScope = TestScope(UnconfinedTestDispatcher() + Job())
        // Use a random file name to avoid "multiple DataStores active for the same file"
        val randomName = "test_${Random.nextInt()}.preferences_pb"
        dataStore = PreferenceDataStoreFactory.createWithPath(
            scope = testScope,
            produceFile = { randomName.toPath() }
        )
        repository = AuthRepositoryImpl(dataStore)
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
        val job = launch {
            repository.getToken().collectLatest {
                storedToken.add(it)
            }
        }

        repository.saveToken("some-token")
        repository.clearToken()

        assertEquals("some-token", storedToken[0])
        assertEquals("", storedToken[1])

        job.cancel()
    }
}
