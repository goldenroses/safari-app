package com.nyenjes.safari.model

import com.google.gson.annotations.SerializedName

class Place(
    @SerializedName("id") var id: Long? = null,
    @SerializedName("title") var title: String? = null,
    @SerializedName("description")var description: String? = null,
    @SerializedName("imageUrl") var imageUrl: String? = null,
    @SerializedName("cardImage") var cardImage: String? = null,
    @SerializedName("content") var content: String? = null
)
