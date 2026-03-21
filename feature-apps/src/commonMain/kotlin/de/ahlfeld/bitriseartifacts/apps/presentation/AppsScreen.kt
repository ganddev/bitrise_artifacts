package de.ahlfeld.bitriseartifacts.apps.presentation

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
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

private val IMAGE_SIZE = 48.dp

@Composable
private fun AppsScreenInternal(
    uiState: AppsUiState,
    uiEventHandler: (AppsUiEvent) -> Unit = {}
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
    ) {
        when (uiState) {
            is Content -> AppsList(
                apps = uiState.apps,
                uiEventHandler = uiEventHandler
            )

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
    apps: List<AppItem>,
    uiEventHandler: (AppsUiEvent) -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(
                color = Color.White
            ),
    ) {
        items(apps, key = { app -> app.title + app.ownerName }) { app ->
            AppItem(
                app = app,
                uiEventHandler = uiEventHandler
            )
        }
    }
}

@Composable
private fun AppItem(
    app: AppItem,
    uiEventHandler: (AppsUiEvent) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .wrapContentHeight()
            .padding(16.dp)
            .clickable {
                uiEventHandler(AppsUiEvent.OnAppItemClicked(item = app))
            },
        verticalAlignment = Alignment.CenterVertically
    ) {
        AsyncImage(
            model = app.avatarUrl,
            contentDescription = app.title,
            modifier = Modifier
                .size(IMAGE_SIZE)
                .clip(CircleShape)
                .background(Color.Gray.copy(alpha = 0.2f))
        )

        Column(
            modifier = Modifier
                .padding(start = 16.dp)
                .weight(1f)
        ) {
            Text(
                text = app.title,
                color = Color.Black,
                style = MaterialTheme.typography.titleMedium
            )
            Text(
                text = app.ownerName,
                color = Color.Black,
                style = MaterialTheme.typography.bodySmall
            )
        }
    }
}
