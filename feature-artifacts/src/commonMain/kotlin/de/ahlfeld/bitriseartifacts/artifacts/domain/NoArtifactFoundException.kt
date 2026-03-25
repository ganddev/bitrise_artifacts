package de.ahlfeld.bitriseartifacts.artifacts.domain

class NoArtifactFoundException(
    private val appSlug: String,
    private val artifactSlug: String,
    private val buildSlug: String
) : Throwable() {

    override val message: String
        get() = "No artifact found for appSlug: $appSlug, artifactSlug: $artifactSlug, buildSlug: $buildSlug"

    override fun toString(): String {
        return message
    }
}