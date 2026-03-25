package de.ahlfeld.bitriseartifacts.presentation.navigation

import kotlinx.serialization.Serializable

@Serializable
sealed interface AppRoute {
    @Serializable
    data object Auth : AppRoute

    @Serializable
    data object AppsList : AppRoute

    @Serializable
    data class BuildsList(val appSlug: String, val appName: String) : AppRoute
}
