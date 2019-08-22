package com.nyenjes.safari.model

import com.google.gson.annotations.SerializedName

class Image(
    @SerializedName("bucketName") val bucketName: String? = null,
    @SerializedName("key") val key: String? = null,
    @SerializedName("size")val size: String? = null,
    @SerializedName("lastModified") val lastModified: String? = null,
    @SerializedName("storageClass") val storageClass: String? = null
)
