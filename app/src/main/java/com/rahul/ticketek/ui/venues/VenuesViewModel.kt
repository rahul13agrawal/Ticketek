package com.rahul.ticketek.ui.venues

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.Priority
import com.google.android.gms.tasks.CancellationTokenSource
import com.rahul.ticketek.data.model.Venue
import com.rahul.ticketek.data.repository.TicketekRepository
import com.rahul.ticketek.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

@HiltViewModel
class VenuesViewModel @Inject constructor(
    private val repository: TicketekRepository,
    private val fusedLocationClient: FusedLocationProviderClient
) : ViewModel() {

    private val _uiState = MutableStateFlow(VenuesUiState.Initial)
    val uiState: StateFlow<VenuesUiState> = _uiState.asStateFlow()

    init {
        fetchVenues()
    }

    fun fetchVenues() {
        viewModelScope.launch {
            _uiState.value = _uiState.value.copy(
                isLoading = true,
                error = null,
            )

            try {
                // Get current location
                val location = fusedLocationClient.getCurrentLocation(
                    Priority.PRIORITY_HIGH_ACCURACY,
                    CancellationTokenSource().token
                ).await()

                if (location != null) {
                    // Fetch venues from API
                    when (val result = repository.getVenues(
                        location.latitude,
                        location.longitude
                    )) {
                        is Result.Success -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                venues = result.data,
                                error = null
                            )
                        }

                        is Result.Error -> {
                            _uiState.value = _uiState.value.copy(
                                isLoading = false,
                                error = result.message
                            )
                        }

                        is Result.Loading -> {
                            // Already loading
                        }
                    }
                } else {
                    _uiState.value = _uiState.value.copy(
                        isLoading = false,
                        error = "Unable to get current location"
                    )
                }
            } catch (_: SecurityException) {
                // Location permission not granted
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Location permission is required"
                )
            } catch (e: Exception) {
                _uiState.value = _uiState.value.copy(
                    isLoading = false,
                    error = "Failed to fetch location: ${e.message}"
                )
            }
        }
    }

    fun selectVenue(venue: Venue) {
        _uiState.value = _uiState.value.copy(selectedVenue = venue)
    }

    fun clearSelectedVenue() {
        _uiState.value = _uiState.value.copy(selectedVenue = null)
    }
}
