package de.ahlfeld.bitriseartifacts.builds.navigation

import kotlinx.serialization.Serializable

@Serializable
data class BuildRoute(val appSlug: String, val appName: String)