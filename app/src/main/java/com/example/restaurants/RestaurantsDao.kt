package com.example.restaurants

import androidx.room.*

/**
 *  defines the methods that are used to interact with the database
 */
@Dao
interface RestaurantsDao {
    @Query("SELECT * FROM restaurants")
    suspend fun getAll(): List<Restaurant>

//    if the restaurants being inserted are already present in the database,
//    we should replace the old ones with the new ones to refresh our cache
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun addAll(restaurants: List<Restaurant>)

    //TODO: update a Restaurant entity through a PartialRestaurant entity
    @Update(entity = Restaurant::class)
    suspend fun update(partialRestaurant: PartialRestaurant)

    //TODO: update the isFavorite field for a list of restaurants instead of only one
    @Update(entity = Restaurant::class)
    suspend fun updateAll(partialRestaurants:
                          List<PartialRestaurant>)

    //TODO: obtains all the restaurants that have their isFavorite field values equal to 1
    @Query("SELECT * FROM restaurants WHERE is_favorite = 1")
            suspend fun getAllFavorited(): List<Restaurant>
}