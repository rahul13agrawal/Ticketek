package com.rahul.ticketek.data.model

import com.google.gson.annotations.SerializedName

data class Venue(
    @SerializedName("code")
    val venueCode: String,
    @SerializedName("name")
    val venueName: String,
    @SerializedName("address")
    val address: String? = null,
    @SerializedName("city")
    val city: String? = null,
    @SerializedName("state")
    val state: String? = null,
    @SerializedName("postcode")
    val postcode: String? = null,
    @SerializedName("latitude")
    val latitude: Double? = null,
    @SerializedName("longitude")
    val longitude: Double? = null,
    @SerializedName("timezone")
    val timezone: String? = null,
    @SerializedName("pax_locations")
    val paxLocations: List<PaxLocation> = emptyList(),
)

data class PaxLocation(
    @SerializedName("name")
    val name: String,
    @SerializedName("gates")
    val gates: List<Gate> = emptyList()
)

data class Gate(
    @SerializedName("name")
    val name: String
)

data class VenueResponse(
    @SerializedName("venues")
    val venues: List<Venue>,
    @SerializedName("total")
    val total: Int? = null
)
