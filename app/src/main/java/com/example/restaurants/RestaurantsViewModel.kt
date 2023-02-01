package com.example.restaurants

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.UnknownHostException


class RestaurantsViewModel(): ViewModel() {
    private var restInterface: RestaurantsApiService

    //TODO:working on caching those restaurants in our database.
    private var restaurantsDao = RestaurantsDb
        .getDaoInstance(RestaurantsApplication.getAppContext())

    val state = mutableStateOf(emptyList<Restaurant>())
    private val errorHandler =
        CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
        }

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl(
                "https://restaurants-5cacf-default-rtdb.firebaseio.com/"
            )
            .build()
        restInterface = retrofit.create(
            RestaurantsApiService::class.java
        )
        getRestaurants()
    }

    /**
     * we passed the restaurants from the local database, which were obtained from
    the toggleFavoriteRestaurant() function
     */
    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch(errorHandler) {
            val updatedRestaurants = toggleFavoriteRestaurant(id, oldValue)
            state.value = updatedRestaurants
        }
    }

    /**
     * Perform updates
     */
    private suspend fun toggleFavoriteRestaurant(id: Int, oldValue: Boolean) =
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

    /**
     * A method where we can specify the correct
    dispatcher for our suspending function
     */
    private suspend fun getAllRestaurants():
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

    /**
     * get the restaurants from the remote server and then cache them inside the local database,
    thereby refreshing their contents
     */
    private suspend fun refreshCache() {
        //TODO: Getting the restaurants from the remote API
        val remoteRestaurants = restInterface.getRestaurants()

        //TODO: Obtain the restaurant that is favoirite
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

    /**
     * To execute the requests asynchronously and on a separate thread,
     * and also allow us to handle any errors that are thrown by Retrofit.
     */
    private fun getRestaurants() {
        viewModelScope.launch(errorHandler) {
            val restaurants = getAllRestaurants()
            state.value = getAllRestaurants()
        }
    }



}