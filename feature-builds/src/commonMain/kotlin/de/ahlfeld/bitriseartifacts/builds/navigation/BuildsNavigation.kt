package de.ahlfeld.bitriseartifacts.builds.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import androidx.navigation.toRoute
import de.ahlfeld.bitriseartifacts.artifactdetail.navigation.ArtifactDetailRoute
import de.ahlfeld.bitriseartifacts.builds.presentation.BuildsNavigationEvent
import de.ahlfeld.bitriseartifacts.builds.presentation.BuildsScreen
import de.ahlfeld.bitriseartifacts.builds.presentation.BuildsViewModel
import org.koin.compose.viewmodel.koinViewModel
import org.koin.core.parameter.parametersOf

fun NavGraphBuilder.buildsScreen(navController: NavController) {
    composable<BuildRoute> { backStackEntry ->
        val route: BuildRoute = backStackEntry.toRoute()
        val buildsViewModel = koinViewModel<BuildsViewModel>(
            parameters = { parametersOf(route.appSlug) }
        )
        LaunchedEffect(buildsViewModel) {
            buildsViewModel.navigationEvents.collect { event ->
                when (event) {
                    is BuildsNavigationEvent.Back -> navController.popBackStack()
                    is BuildsNavigationEvent.ShowArtifactDetails -> navController.navigate(
                        ArtifactDetailRoute(
                            appSlug = event.appSlug,
                            artifactSlugs = event.artifactSlugs,
                            buildSlug = event.buildSlug
                        )
                    )
                }
            }
        }
        BuildsScreen(
            viewModel = buildsViewModel,
        )
    }
}
