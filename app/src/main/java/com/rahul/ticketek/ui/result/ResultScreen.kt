package com.rahul.ticketek.ui.result

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import com.rahul.ticketek.data.model.ScanResponse
import com.rahul.ticketek.ui.scan.ScanUiState
import com.rahul.ticketek.ui.scan.ScanViewModel
import com.rahul.ticketek.ui.theme.TicketekTheme
import kotlinx.coroutines.delay

@Composable
fun ResultScreen(
    onResumeScanning: () -> Unit,
    viewModel: ScanViewModel = hiltViewModel()
) {
    val uiState by viewModel.uiState.collectAsState()

    LaunchedEffect(Unit) {
        delay(5_000)
        viewModel.resetScan()
        onResumeScanning()
    }

    ResultContent(uiState = uiState)
}

@Composable
fun ResultContent(
    uiState: ScanUiState,
    modifier: Modifier = Modifier
) {
    val isSuccess = uiState.scanResult?.success == true
    val message = uiState.scanResult?.message ?: uiState.error ?: "Unknown error"

    Column(
        modifier = modifier
            .fillMaxSize()
            .background(
                if (isSuccess) Color(0xFF4CAF50).copy(alpha = 0.1f)
                else Color(0xFFF44336).copy(alpha = 0.1f)
            )
            .padding(32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {
        Icon(
            imageVector = if (isSuccess) Icons.Default.CheckCircle else Icons.Default.Edit,
            contentDescription = null,
            modifier = Modifier.size(120.dp),
            tint = if (isSuccess) Color(0xFF4CAF50) else Color(0xFFF44336)
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = if (isSuccess) "Entry Allowed" else "Entry Denied",
            style = MaterialTheme.typography.headlineLarge,
            color = if (isSuccess) Color(0xFF4CAF50) else Color(0xFFF44336),
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(16.dp))

        Text(
            text = message,
            style = MaterialTheme.typography.bodyLarge,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurface
        )

        Spacer(modifier = Modifier.height(32.dp))

        Text(
            text = "Resuming scan in a moment...",
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant,
            textAlign = TextAlign.Center
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenSuccessPreview() {
    TicketekTheme {
        ResultContent(
            uiState = ScanUiState(
                isScanning = false,
                scanResult = ScanResponse(
                    status = "Valid Ticket",
                    action = "ENTRY",
                    result = "SUCCESS",
                    concession = 0
                ),
                error = null,
                venueCode = "TEST",
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenDeniedPreview() {
    TicketekTheme {
        ResultContent(
            uiState = ScanUiState(
                isScanning = false,
                scanResult = ScanResponse(
                    status = "Ticket Already Scanned",
                    action = "DENY",
                    result = "FAILURE",
                    concession = 0
                ),
                error = null,
                venueCode = "TEST",
            )
        )
    }
}

@Preview(showBackground = true)
@Composable
fun ResultScreenErrorPreview() {
    TicketekTheme {
        ResultContent(
            uiState = ScanUiState(
                isScanning = false,
                scanResult = null,
                error = "Network connection lost",
                venueCode = "TEST",
            )
        )
    }
}
