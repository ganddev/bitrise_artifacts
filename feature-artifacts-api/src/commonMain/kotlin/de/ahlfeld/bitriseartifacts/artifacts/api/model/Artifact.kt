package de.ahlfeld.bitriseartifacts.artifacts.api.model

data class Artifact(
    val slug: String,
    val title: String,
    val artifactType: String?,
    val publicInstallPageUrl: String
) {
    fun isPublicPageEnabled(): Boolean {
        return publicInstallPageUrl.isNotEmpty()
    }
}
