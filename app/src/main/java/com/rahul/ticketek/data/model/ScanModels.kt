package com.rahul.ticketek.data.model

import com.google.gson.annotations.SerializedName

data class ScanRequest(
    @SerializedName("barcode")
    val barcode: String,
)

data class ScanResponse(
    @SerializedName("status")
    val status: String,
    @SerializedName("action")
    val action: String,
    @SerializedName("result")
    val result: String,
    @SerializedName("concession")
    val concession: Int
) {
    val success: Boolean
        get() = result.equals(
            "SUCCESS",
            ignoreCase = true
        )

    val message: String
        get() = status
}
