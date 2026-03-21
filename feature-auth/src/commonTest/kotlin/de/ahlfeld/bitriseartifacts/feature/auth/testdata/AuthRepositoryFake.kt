package de.ahlfeld.bitriseartifacts.feature.auth.testdata

import de.ahlfeld.bitriseartifacts.feature.auth.domain.repository.AuthRepository
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.flow.update

class AuthRepositoryFake : AuthRepository {
    private val _tokenFlow = MutableStateFlow("")

    override fun getToken(): Flow<String> = _tokenFlow

    override suspend fun saveToken(token: String) {
        _tokenFlow.emit(token)
    }

    override suspend fun clearToken() {
        _tokenFlow.emit("")
    }
}