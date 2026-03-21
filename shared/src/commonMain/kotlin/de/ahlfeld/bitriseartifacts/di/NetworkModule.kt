package de.ahlfeld.bitriseartifacts.di

import de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase.ClearTokenUseCase
import de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase.GetTokenUseCase
import io.ktor.client.HttpClient
import io.ktor.client.plugins.HttpResponseValidator
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.client.plugins.logging.DEFAULT
import io.ktor.client.plugins.logging.LogLevel
import io.ktor.client.plugins.logging.Logger
import io.ktor.client.plugins.logging.Logging
import io.ktor.client.request.header
import io.ktor.http.HttpHeaders
import io.ktor.http.HttpMessageBuilder
import io.ktor.http.HttpStatusCode
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.firstOrNull
import kotlinx.coroutines.runBlocking
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private const val BITRISE_BASE_API_URL = "https://api.bitrise.io/v0.1/"

val networkModule = module {
    single {
        val getTokenUseCase: GetTokenUseCase = get()
        val clearTokenUseCase: ClearTokenUseCase = get()
        HttpClient {
            install(Logging) {
                logger = Logger.DEFAULT
                level = LogLevel.HEADERS
                filter { request ->
                    request.url.host.contains(BITRISE_BASE_API_URL)
                }
            }
            install(ContentNegotiation) {
                json(Json {
                    ignoreUnknownKeys = true
                    prettyPrint = true
                    isLenient = true
                })
            }
            HttpResponseValidator {
                validateResponse { response ->
                    if (response.status == HttpStatusCode.Unauthorized) {
                        clearTokenUseCase()
                    }
                }
            }
            defaultRequest {
                url(BITRISE_BASE_API_URL)
                val token = runBlocking { getTokenUseCase().firstOrNull() }
                if (!token.isNullOrBlank()) {
                    authorizationHeader(token)
                }
            }
        }
    }
}

private fun HttpMessageBuilder.authorizationHeader(token : String) = header(HttpHeaders.Authorization, token)
