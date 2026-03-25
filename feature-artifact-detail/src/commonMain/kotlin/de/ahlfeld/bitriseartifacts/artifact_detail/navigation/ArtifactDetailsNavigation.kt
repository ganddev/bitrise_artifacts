package de.ahlfeld.bitriseartifacts.artifact_detail.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.dialog
import androidx.navigation.toRoute
import de.ahlfeld.bitriseartifacts.artifact_detail.domain.service.UrlOpener
import de.ahlfeld.bitriseartifacts.artifact_detail.presentation.ArtifactDetailsNavigationEvent
import de.ahlfeld.bitriseartifacts.artifact_detail.presentation.ArtifactDetailsScreen
import de.ahlfeld.bitriseartifacts.artifact_detail.presentation.ArtifactDetailsViewModel
import de.ahlfeld.bitriseartifacts.artifactdetail.navigation.ArtifactDetailRoute
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.artifactDetailsScreen(
    navController: NavController
) {
    dialog<ArtifactDetailRoute> { backStackEntry ->
        val route: ArtifactDetailRoute = backStackEntry.toRoute()

        val urlOpener = koinInject<UrlOpener>()
        val artifactDetailsViewModel = koinViewModel<ArtifactDetailsViewModel>()
        LaunchedEffect(artifactDetailsViewModel) {
            artifactDetailsViewModel.navigationEvents.collect { event ->
                when (event) {
                    is ArtifactDetailsNavigationEvent.Back -> navController.popBackStack()
                    is ArtifactDetailsNavigationEvent.OpenUrl -> urlOpener.openUrl(event.url)
                }
            }
        }

        ArtifactDetailsScreen(artifactDetailsViewModel)
    }
}
