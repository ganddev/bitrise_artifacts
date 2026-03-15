package de.ahlfeld.bitriseartifacts.di

import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.plugins.defaultRequest
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.dsl.module

private const val BITRISE_BASE_API_URL = "https://api.bitrise.io/v0.1/"

val networkModule = module {
    single {
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
                // You should provide your API token here
                // header("Authorization", "YOUR_BITRISE_TOKEN")
            }
        }
    }
}
