package de.ahlfeld.bitriseartifacts.apps.navigation

import androidx.compose.runtime.LaunchedEffect
import androidx.navigation.NavController
import androidx.navigation.NavGraphBuilder
import androidx.navigation.compose.composable
import de.ahlfeld.bitriseartifacts.apps.presentation.AppsScreen
import de.ahlfeld.bitriseartifacts.apps.presentation.AppsUiEvent
import de.ahlfeld.bitriseartifacts.apps.presentation.AppsViewModel
import de.ahlfeld.bitriseartifacts.builds.navigation.BuildRoute
import org.koin.compose.viewmodel.koinViewModel

fun NavGraphBuilder.appsScreen(navController: NavController) {
    composable<AppsListRoute> {
        val appsViewModel = koinViewModel<AppsViewModel>()
        LaunchedEffect(appsViewModel) {
            appsViewModel.navigationEvents.collect { event ->
                if (event is AppsUiEvent.OnAppItemClicked) {
                    navController.navigate(
                        BuildRoute(event.item.slug, event.item.title)
                    )
                }
            }
        }
        AppsScreen(viewModel = appsViewModel)
    }
}
