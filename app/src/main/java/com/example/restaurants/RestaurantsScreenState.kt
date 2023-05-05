package com.example.restaurants

// mutation of the object with for instance .copy
data class RestaurantsScreenState(
    val restaurants: List<Restaurant>,
    val isLoading: Boolean,
    val error: String? = null
)
