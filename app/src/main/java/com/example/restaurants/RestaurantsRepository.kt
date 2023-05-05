package com.example.restaurants

import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.UnknownHostException

class RestaurantsRepository {
    private var restInterface: RestaurantsApiService =
        Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl(
                "https://restaurants-5cacf-default-rtdb.firebaseio.com/"
            )
            .build()
            .create(
                RestaurantsApiService::class.java
            )

    private var restaurantsDao = RestaurantsDb
        .getDaoInstance(RestaurantsApplication.getAppContext())

    suspend fun toggleFavoriteRestaurant(id: Int, oldValue: Boolean) =
        withContext(Dispatchers.IO) {
            restaurantsDao.update(
                PartialRestaurant(
                    id = id,
                    isFavorite = !oldValue
                )
            )
            //TODO: return the restaurants from our local database
            restaurantsDao.getAll()
        }

    suspend fun getAllRestaurants():
            List<Restaurant> {
        return withContext(Dispatchers.IO) {
            //TODO: if the user is offline, we catch the exception thrown by Retrofit
            try {
                refreshCache()
            } catch (e: Exception) {
                //TODO: if the user is offline, we catch the exception thrown by Retrofit
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is HttpException -> {
                        //TODO: return the cached restaurants from the Room database
                        if (restaurantsDao.getAll().isEmpty())
                            throw Exception("Something went wrong. " + "We have no data.")
                    }
                    else -> throw e
                }
            }
            // our app tries to display the restaurants from the local database in any condition
            return@withContext restaurantsDao.getAll()
        }
    }

    suspend fun getRemoteRestaurant(id: Int):
            Restaurant {
        return withContext(Dispatchers.IO) {
            try {
                val responseMap = restInterface.getRestaurant(id)
                return@withContext responseMap.values.first()
            } catch (e: Exception) {
                when (e) {
                    is UnknownHostException,
                    is ConnectException,
                    is HttpException -> {
                        return@withContext restaurantsDao.getRestaurantDetails(id)
                    }
                    else -> throw e
                }
            }
        }
    }

    private suspend fun refreshCache() {
        //TODO: Getting the restaurants from the remote API
        val remoteRestaurants = restInterface.getRestaurants()

        //TODO: Obtain the restaurant that is favorite
        val favoriteRestaurants = restaurantsDao
            .getAllFavorited()

        //TODO: override the isFavorite field (to false) of the existing restaurants that have
        // the same ID as remoteRestaurants.
        restaurantsDao.addAll(remoteRestaurants)

        //TODO: partially updated all the restaurants within Room
        restaurantsDao.updateAll(
            favoriteRestaurants.map {
                PartialRestaurant(it.id, true)
            })
    }
}