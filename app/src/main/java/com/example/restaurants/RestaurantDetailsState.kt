package com.example.restaurants

data class RestaurantDetailsState(
    val restaurant: Restaurant?,
    val title: String?,
    val description: String?,
    val isLoading: Boolean = false,
    val error: String? = null
)