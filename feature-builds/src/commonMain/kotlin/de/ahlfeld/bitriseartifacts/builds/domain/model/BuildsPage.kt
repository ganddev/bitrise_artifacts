package de.ahlfeld.bitriseartifacts.builds.domain.model

internal data class BuildsPage(
    val builds: List<Build>,
    val next: String?
)
