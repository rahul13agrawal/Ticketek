package com.rahul.ticketek.ui.scan

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.rahul.ticketek.data.repository.TicketekRepository
import com.rahul.ticketek.util.Result
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class ScanViewModel @Inject constructor(
    private val repository: TicketekRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ScanUiState.initial)
    val uiState: StateFlow<ScanUiState> = _uiState.asStateFlow()

    fun setVenueCode(code: String) {
        _uiState.value = _uiState.value.copy(venueCode = code)
    }

    fun scanBarcode(barcode: String) {
        if (_uiState.value.isScanning.not()) {
            return
        }

        if (_uiState.value.venueCode.isEmpty()) {
            _uiState.value = _uiState.value.copy(
                error = "Venue code not set",
                isScanning = false
            )
            return
        }

        _uiState.value = _uiState.value.copy(isScanning = false)

        viewModelScope.launch {
            when (val result = repository.scanTicket(
                _uiState.value.venueCode,
                barcode
            )) {
                is Result.Success -> {
                    _uiState.value = _uiState.value.copy(
                        scanResult = result.data,
                        error = null
                    )
                }

                is Result.Error -> {
                    _uiState.value = _uiState.value.copy(
                        error = result.message,
                        scanResult = null
                    )
                }

                is Result.Loading -> {
                    // Loading state
                }
            }
        }
    }

    fun resetScan() {
        _uiState.value = _uiState.value.copy(
            isScanning = true,
            scanResult = null,
            error = null
        )
    }
}
