package com.example.restaurants

import retrofit2.Call
import retrofit2.http.GET

/**
 * defines the HTTP operations that are executed between our app and the database
 */
interface RestaurantsApiServices {
    @GET("restaurants.json")
    // The Call object represents the invocation of a Retrofit method that sends network requests
    // and receives a response
    fun getRestaurants(): Call<List<Restaurant>>
}