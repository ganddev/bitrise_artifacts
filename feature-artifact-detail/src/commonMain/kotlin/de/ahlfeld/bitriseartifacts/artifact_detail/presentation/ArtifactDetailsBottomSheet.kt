package de.ahlfeld.bitriseartifacts.artifact_detail.presentation

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Download
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import de.ahlfeld.bitriseartifacts.artifact_detail.domain.model.ArtifactDetails


@Composable
internal fun ArtifactDetailsScreen(
    viewModel: ArtifactDetailsViewModel
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val uiEventHandler = viewModel::handleUiEvent

    ArtifactDetailsBottomScreenInternal(
        uiState = uiState,
        uiEventHandler = uiEventHandler
    )
}

@Composable
@OptIn(ExperimentalMaterial3Api::class)
private fun ArtifactDetailsBottomScreenInternal(
    uiState: ArtifactDetailsUiState,
    uiEventHandler: (ArtifactDetailsUiEvent) -> Unit
) {
    when (uiState) {
        is ArtifactDetailsUiState.Visible -> {
            val sheetState = rememberModalBottomSheetState()

            ModalBottomSheet(
                onDismissRequest = { uiEventHandler(ArtifactDetailsUiEvent.OnDismissed) },
                sheetState = sheetState,
                shape = RoundedCornerShape(topStart = 16.dp, topEnd = 16.dp)
            ) {
                ArtifactDetailsBottomSheetContent(
                    artifactDetails = uiState.artifacts,
                    uiEventHandler = uiEventHandler,
                    modifier = Modifier.padding(bottom = 32.dp)
                )
            }
        }

        else -> {}
    }
}

@Composable
private fun ArtifactDetailsBottomSheetContent(
    artifactDetails: List<ArtifactDetails>,
    uiEventHandler: (ArtifactDetailsUiEvent) -> Unit,
    modifier: Modifier = Modifier
) {
    Column(modifier = modifier.fillMaxWidth()) {
        Text(
            text = "Artifacts",
            style = MaterialTheme.typography.headlineSmall,
            fontWeight = FontWeight.Bold,
            modifier = Modifier
                .align(Alignment.CenterHorizontally)
                .padding(bottom = 16.dp)
        )

        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            items(artifactDetails) { artifact ->
                ArtifactItem(
                    artifact = artifact,
                    uiEventHandler = uiEventHandler
                )
            }
        }
    }
}

@Composable
private fun ArtifactItem(
    artifact: ArtifactDetails,
    uiEventHandler: (ArtifactDetailsUiEvent) -> Unit,
) {
    val isClickable = artifact.publicUrl.isNotEmpty()

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(enabled = isClickable) {
                uiEventHandler(
                    ArtifactDetailsUiEvent.OnArtifactClicked(
                        artifact
                    )
                )
            }
            .padding(12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column(
            modifier = Modifier
                .weight(1f)
                .padding(end = 8.dp)
        ) {
            Text(
                text = artifact.title,
                style = MaterialTheme.typography.bodyLarge,
                fontWeight = FontWeight.SemiBold
            )
        }

        if (isClickable) {
            Icon(
                imageVector = Icons.Default.Download,
                contentDescription = "Download artifact",
                tint = MaterialTheme.colorScheme.primary
            )
        }
    }
}

