package de.ahlfeld.bitriseartifacts

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import de.ahlfeld.bitriseartifacts.apps.presentation.AppsScreen
import de.ahlfeld.bitriseartifacts.apps.presentation.AppsViewModel
import de.ahlfeld.bitriseartifacts.feature.auth.domain.usecase.GetTokenUseCase
import de.ahlfeld.bitriseartifacts.feature.auth.presentation.AuthScreen
import de.ahlfeld.bitriseartifacts.feature.auth.presentation.AuthViewModel
import org.koin.compose.koinInject
import org.koin.compose.viewmodel.koinViewModel
import org.jetbrains.compose.resources.ExperimentalResourceApi

@OptIn(ExperimentalResourceApi::class)
@Composable
fun App() {
    MaterialTheme {
        Surface(
            modifier = Modifier.fillMaxSize(),
            color = MaterialTheme.colorScheme.background
        ) {
            val getTokenUseCase: GetTokenUseCase = koinInject()
            val tokenFlow = remember { getTokenUseCase() }
            val token by tokenFlow.collectAsState(initial = null)

            if (token.isNullOrBlank()) {
                val authViewModel = koinViewModel<AuthViewModel>()
                AuthScreen(
                    viewModel = authViewModel,
                    onTokenSaved = { /* Token saved, flow will emit new value */ }
                )
            } else {
                val viewModel = koinViewModel<AppsViewModel>()
                AppsScreen(viewModel = viewModel)
            }
        }
    }
}
