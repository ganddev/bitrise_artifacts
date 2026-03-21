package de.ahlfeld.bitriseartifacts.di

import de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase.GetTokenUseCase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.request.header
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private const val BITRISE_BASE_API_URL = "https://api.bitrise.io/v0.1/"

val networkModule = module {
    single {
        val getTokenUseCase: GetTokenUseCase = get()
        HttpClient {
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            defaultRequest {
                url(BITRISE_BASE_API_URL)
                val token = runBlocking { getTokenUseCase().firstOrNull() }
                if (token != null) {
                    header("Authorization", token)
                }
            }
        }
    }
}
