package de.ahlfeld.bitriseartifacts.feature.auth.domain.repository

import kotlinx.coroutines.flow.Flow

interface AuthRepository {
    fun getToken(): Flow<String?>
    suspend fun saveToken(token: String)
    suspend fun clearToken()
}
