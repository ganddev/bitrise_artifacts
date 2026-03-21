package de.ahlfeld.bitriseartifacts.builds.domain.model

data class Build(
    val buildNumber: Int,
    val branch: String,
    val triggeredAt: String,
    val finishedAt: String?,
    val commitHash: String?,
    val slug: String,
    val status: Int
)
