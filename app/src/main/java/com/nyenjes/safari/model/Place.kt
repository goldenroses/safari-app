package com.nyenjes.safari.model

import com.google.gson.annotations.SerializedName

class Place(
    @SerializedName("id") val id: Long? = null,
    @SerializedName("title") val title: String? = null,
    @SerializedName("description")val description: String? = null,
    @SerializedName("imageUrl") val imageUrl: String? = null,
    @SerializedName("cardImage") val cardImage: String? = null,
    @SerializedName("content") val content: String? = null
)
