package com.rahul.ticketek.data.api

import com.rahul.ticketek.data.model.ScanRequest
import com.rahul.ticketek.data.model.ScanResponse
import com.rahul.ticketek.data.model.VenueResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.Headers
import retrofit2.http.POST
import retrofit2.http.Path
import retrofit2.http.Query

interface TicketekApi {

    @Headers(
        "x-api-key: TEq5Mddna23xSNsoDeYt8aP02BJHrvoa6X07nEuD",
        "authorization: Basic Yhd9X=38D88!",
        "content-type: application/json",
        "accept-language: en"
    )
    @GET("venues/")
    suspend fun getVenues(
        @Query("latitude") latitude: Double,
        @Query("longitude") longitude: Double
    ): Response<VenueResponse>

    @Headers(
        "x-api-key: TEq5Mddna23xSNsoDeYt8aP02BJHrvoa6X07nEuD",
        "authorization: Basic Yhd9X=38D88!",
        "content-type: application/json",
        "accept-language: en",
        "Accept: */*"
    )
    @POST("venues/{venue_code}/pax/entry/scan")
    suspend fun scanTicket(
        @Path("venue_code") venueCode: String,
        @Body scanRequest: ScanRequest
    ): Response<ScanResponse>
}
