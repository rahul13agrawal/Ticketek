package com.rahul.ticketek.ui.venues

import com.rahul.ticketek.data.model.Venue

data class VenuesUiState(
    val isLoading: Boolean,
    val venues: List<Venue>,
    val error: String?,
    val selectedVenue: Venue?,
) {
    companion object {
        val Initial = VenuesUiState(
            isLoading = false,
            venues = emptyList(),
            error = null,
            selectedVenue = null,
        )
    }
}
