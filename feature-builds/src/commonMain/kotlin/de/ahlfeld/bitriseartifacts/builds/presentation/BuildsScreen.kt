package de.ahlfeld.bitriseartifacts.builds.presentation

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp

@Composable
fun BuildsScreen(
    viewModel: BuildsViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    BuildsScreenInternal(uiState = uiState, uiEventHandler = viewModel::handleUiEvent)
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BuildsScreenInternal(
    uiState: BuildsUiState,
    uiEventHandler: (BuildsUiEvent) -> Unit = {}
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Builds") },
                navigationIcon = {
                    IconButton(onClick = {
                        uiEventHandler(BuildsUiEvent.OnBackClicked)
                    }
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back"
                        )
                    }
                }
            )
        }
    ) { padding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(padding)
        ) {
            when (uiState) {
                is BuildsUiState.Loading -> CircularProgressIndicator(
                    modifier = Modifier.align(
                        Alignment.Center
                    )
                )

                is BuildsUiState.Error -> Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )

                is BuildsUiState.Content -> BuildsList(uiState.builds)
            }
        }
    }
}

@Composable
private fun BuildsList(builds: List<BuildItem>) {
    if(builds.isEmpty()) {
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Text(
                text = "Select an app to see builds",
                style = MaterialTheme.typography.titleLarge
            )
        }
    }
    LazyColumn(
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(builds, key = { it.buildNumber }) { build ->
            BuildItemRow(build)
        }
    }
}

@Composable
private fun BuildItemRow(build: BuildItem) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                Text(
                    text = "#${build.buildNumber}",
                    style = MaterialTheme.typography.titleMedium
                )
                Text(
                    text = build.branch,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.primary
                )
            }
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Triggered: ${build.triggeredAt}",
                style = MaterialTheme.typography.bodySmall
            )
            build.finishedAt?.let {
                Text(
                    text = "Finished: $it",
                    style = MaterialTheme.typography.bodySmall
                )
            }
            build.commitHash?.let {
                Text(
                    text = "Commit: ${it.take(7)}",
                    style = MaterialTheme.typography.bodySmall,
                    color = Color.Gray
                )
            }
        }
    }
}

@Composable
@Preview
private fun BuildsScreenPreview(
    @PreviewParameter(BuildsUiStatePreviewParameterProvider::class) uiState : BuildsUiState
) {
    MaterialTheme {
        BuildsScreenInternal(
            uiState = uiState
        )
    }
}
