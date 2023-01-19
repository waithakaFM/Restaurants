package com.example.restaurants

import com.google.gson.annotations.SerializedName

/**
 * Annotate the Restaurant data class fields with special serialization matchers that
tell Retrofit which keys should be matched with each field.
 */
data class Restaurant(
    @SerializedName("r_id")
    val id: Int,
    @SerializedName("r_title")
    val title: String,
    @SerializedName("r_description")
    val description: String,
    var isFavorite: Boolean = false)
