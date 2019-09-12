package com.nyenjes.safari.model

data class Review(
    var id: Long? = null,
    var title: String? = null,
    var rating: String? = null,
    var comment: String? = null,
    var person: Person? = null,
    var place: Place? = null
)
