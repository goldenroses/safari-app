package com.nyenjes.safari.model

data class Guide(
    var id: Long? = null,
    var title: String? = null,
    var introduction: String? = null,
    var personId: Int? = null,
    var categoryId: Int? = null,
    var reviewId: Int? = null
)
