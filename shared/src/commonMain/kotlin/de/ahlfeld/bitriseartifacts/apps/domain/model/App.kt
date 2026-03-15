package de.ahlfeld.bitriseartifacts.apps.domain.model

data class App(
    val slug: String,
    val title: String,
    val ownerName: String,
    val avatarUrl: String?
)
