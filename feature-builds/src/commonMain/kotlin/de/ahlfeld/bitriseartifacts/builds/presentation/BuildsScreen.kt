package de.ahlfeld.bitriseartifacts.builds.presentation

import androidx.compose.foundation.clickable
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
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Android
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
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.tooling.preview.PreviewParameter
import androidx.compose.ui.unit.dp

@Composable
internal fun BuildsScreen(
    viewModel: BuildsViewModel,
) {
    val uiState by viewModel.uiState.collectAsState()

    BuildsScreenInternal(
        uiState = uiState,
        uiEventHandler = viewModel::handleUiEvent,
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun BuildsScreenInternal(
    uiState: BuildsUiState,
    uiEventHandler: (BuildsUiEvent) -> Unit = {},
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

                is BuildsUiState.Content -> BuildsList(
                    builds = uiState.builds,
                    hasMore = uiState.hasMore,
                    isLoadingMore = uiState.isLoadingMore,
                    uiEventHandler = uiEventHandler
                )
            }
        }
    }
}

@Composable
private fun BuildsList(
    builds: List<BuildItem>,
    hasMore: Boolean,
    isLoadingMore: Boolean,
    uiEventHandler: (BuildsUiEvent) -> Unit = {},
) {
    if (builds.isEmpty()) {
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

    val listState = rememberLazyListState()

    val shouldLoadMore = remember {
        derivedStateOf {
            val lastVisibleItem = listState.layoutInfo.visibleItemsInfo.lastOrNull()
                ?: return@derivedStateOf false

            lastVisibleItem.index >= listState.layoutInfo.totalItemsCount - 2
        }
    }

    LaunchedEffect(shouldLoadMore.value) {
        if (shouldLoadMore.value && hasMore && !isLoadingMore) {
            uiEventHandler(BuildsUiEvent.OnPageEnd)
        }
    }

    LazyColumn(
        state = listState,
        modifier = Modifier.fillMaxSize(),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(8.dp)
    ) {
        items(builds, key = { it.buildSlug }) { build ->
            BuildItemRow(
                build = build,
                onClick = {
                    uiEventHandler(
                        BuildsUiEvent.OnBuildClicked(
                            artifactSlugs = build.artifactSlugs,
                            buildSlug = build.buildSlug
                        )
                    )
                }
            )
        }

        if (isLoadingMore) {
            item(key = "loading_more") {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    CircularProgressIndicator(modifier = Modifier.size(24.dp))
                }
            }
        }
    }
}

@Composable
private fun BuildItemRow(
    build: BuildItem,
    onClick: () -> Unit = {}
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Text(
                        text = "#${build.buildNumber}",
                        style = MaterialTheme.typography.titleMedium
                    )
                    if (build.artifactSlugs.isNotEmpty()) {
                        Spacer(modifier = Modifier.size(8.dp))
                        Icon(
                            imageVector = Icons.Default.Android,
                            contentDescription = "APK Available",
                            modifier = Modifier.size(20.dp),
                            tint = Color(0xFF3DDC84) // Android Green
                        )
                    }
                }
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
    @PreviewParameter(BuildsUiStatePreviewParameterProvider::class) uiState: BuildsUiState
) {
    MaterialTheme {
        BuildsScreenInternal(
            uiState = uiState,
        )
    }
}
