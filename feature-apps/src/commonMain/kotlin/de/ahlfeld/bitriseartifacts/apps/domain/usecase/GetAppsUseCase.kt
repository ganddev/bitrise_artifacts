package de.ahlfeld.bitriseartifacts.apps.domain.usecase

import de.ahlfeld.bitriseartifacts.apps.domain.model.App
import de.ahlfeld.bitriseartifacts.apps.domain.repository.AppsRepository

class GetAppsUseCase(private val repository: AppsRepository) {
    suspend operator fun invoke(): Result<List<App>> = runCatching {
        repository.getApps()
    }
}
