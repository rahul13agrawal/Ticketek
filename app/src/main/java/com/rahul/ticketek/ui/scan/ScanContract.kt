package com.rahul.ticketek.ui.scan

import com.rahul.ticketek.data.model.ScanResponse

data class ScanUiState(
    val isScanning: Boolean,
    val scanResult: ScanResponse?,
    val error: String?,
    val venueCode: String,
) {
    companion object {
        val initial = ScanUiState(
            isScanning = true,
            scanResult = null,
            error = null,
            venueCode = "",
        )
    }
}