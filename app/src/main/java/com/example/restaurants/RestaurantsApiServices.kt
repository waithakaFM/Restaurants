package com.example.restaurants

import retrofit2.Call
import retrofit2.http.GET

/**
 * defines the HTTP operations that are executed between our app and the database
 */
interface RestaurantsApiService {
    @GET("restaurants.json")
    //TODO: transform the network requests to suspending work that isn't
    // blocking the main thread of the application
    suspend fun getRestaurants(): List<Restaurant>
}