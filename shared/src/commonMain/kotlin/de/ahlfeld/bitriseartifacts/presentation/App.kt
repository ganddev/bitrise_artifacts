package de.ahlfeld.bitriseartifacts.presentation

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.safeDrawingPadding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.rememberNavController
import de.ahlfeld.bitriseartifacts.apps.navigation.AppsListRoute
import de.ahlfeld.bitriseartifacts.apps.navigation.appsScreen
import de.ahlfeld.bitriseartifacts.apps.presentation.AppsScreen
import de.ahlfeld.bitriseartifacts.apps.presentation.AppsUiEvent
import de.ahlfeld.bitriseartifacts.apps.presentation.AppsViewModel
import de.ahlfeld.bitriseartifacts.builds.navigation.BuildRoute
import de.ahlfeld.bitriseartifacts.builds.navigation.buildsScreen
import de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase.GetTokenUseCase
import de.ahlfeld.bitriseartifacts.feature.auth.presentation.AuthScreen
import de.ahlfeld.bitriseartifacts.feature.auth.presentation.AuthViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize()
                .safeDrawingPadding(),
            color = MaterialTheme.colorScheme.background
        ) {
            val getTokenUseCase: GetTokenUseCase = koinInject()
            val token by getTokenUseCase().collectAsStateWithLifecycle(null)

            if (token.isNullOrBlank()) {
                val authViewModel = koinViewModel<AuthViewModel>()
                AuthScreen(viewModel = authViewModel)
            } else {
                AuthenticatedApp()
            }
        }
    }
}

@Composable
fun AuthenticatedApp() {
    val navController = rememberNavController()

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isExpanded = maxWidth > 600.dp

        Row(modifier = Modifier.fillMaxSize()) {
            if (isExpanded) {
                Box(modifier = Modifier.weight(0.4f)) {
                    val appsViewModel = koinViewModel<AppsViewModel>()
                    LaunchedEffect(appsViewModel) {
                        appsViewModel.navigationEvents.collect { event ->
                            if (event is AppsUiEvent.OnAppItemClicked) {
                                navController.navigate(
                                    BuildRoute(
                                        appSlug = event.item.slug,
                                        appName = event.item.title
                                    )
                                ) {
                                    popUpTo(AppsListRoute) {
                                        saveState = true
                                    }
                                    launchSingleTop = true
                                    restoreState = true
                                }
                            }
                        }
                    }
                    AppsScreen(
                        viewModel = appsViewModel,
                    )
                }
            }

            Box(modifier = Modifier.weight(if (isExpanded) 0.6f else 1f)) {
                NavHost(
                    navController = navController,
                    startDestination = AppsListRoute
                ) {
                    appsScreen(navController)
                    buildsScreen(navController)
                }
            }
        }
    }
}
