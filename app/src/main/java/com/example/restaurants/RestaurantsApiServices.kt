package com.example.restaurants

import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

/**
 * defines the HTTP operations that are executed between our app and the database
 */
interface RestaurantsApiService {
    @GET("restaurants.json")
    suspend fun getRestaurants(): List<Restaurant>

    //TODO: get the details of one restaurant filtering the elements by the value of their r_id key
    /**
     * orderBy=r_id to instruct Firebase to filter the elements by their r_id field.
     * equalTo to let Firebase know the value of the r_id field of the restaurant
    element that we're looking for.
     */
    @GET("restaurants.json?orderBy=\"r_id\"")
    suspend fun getRestaurant(
        @Query("equalTo") id: Int): Map<String, Restaurant>
}