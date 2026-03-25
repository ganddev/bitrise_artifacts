package de.ahlfeld.bitriseartifacts.artifact_detail.domain.model

internal class NoArtifactDetailsFoundException(
    private val appSlug : String,
    private val artifactSlug : String,
    private val buildSlug : String
) : Exception() {
    override val message: String
        get() = "No artifact details found for appSlug: $appSlug, artifactSlug: $artifactSlug, buildSlug: $buildSlug"

    override fun toString(): String {
        return message
    }
}