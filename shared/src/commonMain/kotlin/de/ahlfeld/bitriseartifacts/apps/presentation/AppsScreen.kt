package de.ahlfeld.bitriseartifacts.apps.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp
import de.ahlfeld.bitriseartifacts.apps.presentation.AppsUiState.Content
import de.ahlfeld.bitriseartifacts.apps.presentation.AppsUiState.Error
import de.ahlfeld.bitriseartifacts.apps.presentation.AppsUiState.Loading

@Composable
fun AppsScreen(viewModel: AppsViewModel) {
    val uiState by viewModel.uiState.collectAsState()

    AppsScreenInternal(
        uiState = uiState,
        uiEventHandler = viewModel::handleUiEvent
    )
}

@Composable
private fun AppsScreenInternal(
    uiState: AppsUiState,
    uiEventHandler: (AppsUiEvent) -> Unit = {}
) {
    Box(modifier = Modifier.fillMaxSize()) {
        when (uiState) {
            is Content -> AppsList(apps = uiState.apps)
            is Error -> {
                Text(
                    text = uiState.message,
                    color = MaterialTheme.colorScheme.error,
                    modifier = Modifier.align(Alignment.Center)
                )
            }

            is Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
        }
    }
}

@Composable
private fun AppsList(
    apps: List<AppItem>
) {
    LazyColumn {
        items(apps) { app ->
            AppItem(app = app)
        }
    }
}

@Composable
private fun AppItem(app: AppItem) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        if (app.avatarUrl != null) {
            /*RemoteImage(
                url = app.avatarUrl,
                contentDescription = app.title,
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
            )*/
        } else {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(text = app.title, style = MaterialTheme.typography.titleMedium)
            Text(text = app.ownerName, style = MaterialTheme.typography.bodySmall)
        }
    }
}


@Composable
@Preview
private fun AppsScreenPreview(
    @PreviewParameter(AppsUiStateProvider::class) uiState: AppsUiState
) {
    AppsScreenInternal(uiState)
}