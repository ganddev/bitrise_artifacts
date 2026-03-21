package de.ahlfeld.bitriseartifacts.apps.domain.repository

import de.ahlfeld.bitriseartifacts.apps.domain.model.App

interface AppsRepository {
    suspend fun getApps(): List<App>
}
