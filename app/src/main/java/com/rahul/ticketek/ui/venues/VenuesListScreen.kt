package com.rahul.ticketek.ui.venues

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
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
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahul.ticketek.data.model.Venue
import com.rahul.ticketek.ui.loading.LoadingScreen
import com.rahul.ticketek.ui.theme.TicketekTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VenuesListScreen(
    onVenueSelected: (Venue) -> Unit,
    viewModel: VenuesViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(uiState.selectedVenue) {
        uiState.selectedVenue?.let { venue ->
            onVenueSelected(venue)
            viewModel.clearSelectedVenue()
        }
    }

    VenuesListContent(
        uiState = uiState,
        onRefresh = { viewModel.fetchVenues() },
        onVenueClick = { venue -> viewModel.selectVenue(venue) }
    )
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun VenuesListContent(
    uiState: VenuesUiState,
    onRefresh: () -> Unit,
    onVenueClick: (Venue) -> Unit,
    modifier: Modifier = Modifier
) {
    Scaffold(
        modifier = modifier,
        topBar = {
            TopAppBar(
                title = { Text("Nearby Venues") },
                actions = {
                    IconButton(onClick = onRefresh) {
                        Icon(
                            Icons.Default.Refresh,
                            contentDescription = "Refresh"
                        )
                    }
                }
            )
        }
    ) { paddingValues ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            when {
                uiState.isLoading -> {
                    LoadingScreen()
                }

                uiState.error != null -> {
                    ErrorContent(
                        message = uiState.error,
                        onRetry = onRefresh
                    )
                }

                uiState.venues.isEmpty() -> {
                    EmptyContent(onRetry = onRefresh)
                }

                else -> {
                    VenuesList(
                        venues = uiState.venues,
                        onVenueClick = onVenueClick
                    )
                }
            }
        }
    }
}

@Composable
fun VenuesList(
    venues: List<Venue>,
    onVenueClick: (Venue) -> Unit
) {
    LazyColumn(
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        items(venues) { venue ->
            VenueCard(
                venue = venue,
                onClick = { onVenueClick(venue) }
            )
        }
    }
}

@Composable
fun VenueCard(
    venue: Venue,
    onClick: () -> Unit
) {
    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
        ) {
            Text(
                text = venue.venueName,
                style = MaterialTheme.typography.titleLarge,
                color = MaterialTheme.colorScheme.onSurface
            )

            Spacer(modifier = Modifier.height(8.dp))

            if (!venue.address.isNullOrEmpty()) {
                Text(
                    text = venue.address,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
            }

            if (!venue.city.isNullOrEmpty()) {
                Text(
                    text = venue.city,
                    style = MaterialTheme.typography.bodyMedium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
                Spacer(modifier = Modifier.height(4.dp))
            }
        }
    }
}

@Composable
fun ErrorContent(
    message: String,
    onRetry: () -> Unit
) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Text(
            text = "⚠️",
            style = MaterialTheme.typography.displayLarge
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "Error",
            style = MaterialTheme.typography.headlineMedium,
            color = MaterialTheme.colorScheme.error
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onRetry) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Retry")
        }
    }
}

@Composable
fun EmptyContent(onRetry: () -> Unit) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = Icons.Default.LocationOn,
            contentDescription = null,
            modifier = Modifier.height(100.dp),
            tint = MaterialTheme.colorScheme.primary
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = "No Venues Found",
            style = MaterialTheme.typography.headlineMedium
        )

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = "No venues found within 1km of your location.",
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(24.dp))

        Button(onClick = onRetry) {
            Icon(
                Icons.Default.Refresh,
                contentDescription = null
            )
            Spacer(modifier = Modifier.padding(4.dp))
            Text("Retry")
        }
    }
}

@Preview(showBackground = true)
@Composable
fun VenuesListContentPreview() {
    TicketekTheme {
        VenuesListContent(
            uiState = VenuesUiState(
                isLoading = false,
                venues = listOf(
                    Venue(
                        venueCode = "STAD1",
                        venueName = "Sydney Cricket Ground",
                        address = "Driver Ave",
                        city = "Moore Park",
                    ),
                    Venue(
                        venueCode = "STAD2",
                        venueName = "Allianz Stadium",
                        address = "Driver Ave",
                        city = "Moore Park",
                    )
                ),
                error = null,
                selectedVenue = null
            ),
            onRefresh = {},
            onVenueClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VenuesListEmptyPreview() {
    TicketekTheme {
        VenuesListContent(
            uiState = VenuesUiState.Initial,
            onRefresh = {},
            onVenueClick = {}
        )
    }
}

@Preview(showBackground = true)
@Composable
fun VenuesListErrorPreview() {
    TicketekTheme {
        VenuesListContent(
            uiState = VenuesUiState(
                isLoading = false,
                venues = emptyList(),
                error = "Network timeout occurred",
                selectedVenue = null
            ),
            onRefresh = {},
            onVenueClick = {}
        )
    }
}
