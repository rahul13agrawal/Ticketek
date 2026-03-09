package com.rahul.ticketek.data.repository

import com.rahul.ticketek.data.api.TicketekApi
import com.rahul.ticketek.data.model.ScanRequest
import com.rahul.ticketek.data.model.ScanResponse
import com.rahul.ticketek.data.model.Venue
import com.rahul.ticketek.util.Result
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.IOException
import javax.inject.Inject
import javax.inject.Singleton

@Singleton
class TicketekRepository @Inject constructor(
    private val api: TicketekApi
) {

    suspend fun getVenues(latitude: Double, longitude: Double): Result<List<Venue>> {
        return withContext(Dispatchers.IO) {
            try {
                val response = api.getVenues(
                    latitude = latitude,
                    longitude = longitude
                )
                if (response.isSuccessful) {
                    val venues = response.body()?.venues ?: emptyList()
                    Result.Success(venues)
                } else {
                    Result.Error("Failed to fetch venues: ${response.code()} ${response.message()}")
                }
            } catch (e: IOException) {
                Result.Error(
                    "Network error. Please check your connection.",
                    e
                )
            } catch (e: Exception) {
                Result.Error(
                    "An unexpected error occurred: ${e.message}",
                    e
                )
            }
        }
    }

    suspend fun scanTicket(venueCode: String, barcode: String): Result<ScanResponse> {
        return withContext(Dispatchers.IO) {
            try {
                val scanRequest = ScanRequest(barcode = barcode)
                val response = api.scanTicket(
                    venueCode,
                    scanRequest
                )
                if (response.isSuccessful) {
                    response.body()?.let {
                        Result.Success(it)
                    } ?: Result.Error("Empty response from server")
                } else {
                    Result.Error("Scan failed: ${response.code()} ${response.message()}")
                }
            } catch (e: IOException) {
                Result.Error(
                    "Network error. Please check your connection.",
                    e
                )
            } catch (e: Exception) {
                Result.Error(
                    "An unexpected error occurred: ${e.message}",
                    e
                )
            }
        }
    }
}
