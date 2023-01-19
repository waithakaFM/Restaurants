package com.example.restaurants

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RestaurantsViewModel(
    private val stateHandle: SavedStateHandle):
    ViewModel() {

    // Will call upon to execute the desired network requests
    private var restInterface: RestaurantsApiServices

    // to hold a reference to our enqueued Call object
    private lateinit var restaurantsCall: Call<List<Restaurant>>

    val state = mutableStateOf(emptyList<Restaurant>())

    //TODO: Executing GET requests to the Firebase REST API
    init {
        val retrofit: Retrofit = Retrofit.Builder()
            // tell Retrofit that we want the JSON to be deserialized with the GSON converter
            .addConverterFactory(
                GsonConverterFactory.create()
            )
            .baseUrl(
                "https://restaurants-5cacf-default-rtdb.firebaseio.com/"
            )
            .build()
        restInterface = retrofit.create(
            RestaurantsApiServices::class.java
        )
        getRestaurants()
    }

    /**
     * this  will allow theRestaurantsViewModel to mutate the value of the state variable every time
    we try to toggle the favorite status of a restaurant
     */
    fun toggleFavorite(id: Int) {

        val restaurants = state.value.toMutableList()
        val itemIndex =
            restaurants.indexOfFirst { it.id == id }
        val item = restaurants[itemIndex]
        restaurants[itemIndex] =
            item.copy(isFavorite = !item.isFavorite)
        storeSelection(restaurants[itemIndex])
        state.value = restaurants
    }

    /**
     * This method receives a Restaurant object whose isFavorite property has just been altered,
    and saves that selection inside the SavedStateHandle object provided by the
    RestaurantsViewModel class
     */
    private fun storeSelection(item: Restaurant) {
        val savedToggled = stateHandle
            .get<List<Int>?>(FAVORITES)
            .orEmpty().toMutableList()
        if (item.isFavorite) savedToggled.add(item.id)
        else savedToggled.remove(item.id)
        stateHandle[FAVORITES] = savedToggled
    }


    /**
     * To execute the requests asynchronously and on a separate thread,
     * and also allow us to handle any errors that are thrown by Retrofit.
     */
    private fun getRestaurants() {
        // Enqueue() runs the network request asynchronously on a separate thread
        restaurantsCall = restInterface.getRestaurants()
        restaurantsCall.enqueue(
            object : Callback<List<Restaurant>> {
                // invoked when the network request succeeds
                override fun onResponse(
                    call: Call<List<Restaurant>>,
                    response: Response<List<Restaurant>>
                ) {
                    //This returns the deserialized body of the HTTP response.
                    response.body()?.let { restaurants ->
                        state.value =
                            restaurants.restoreSelections()
                    }
                }
                override fun onFailure(
                    call: Call<List<Restaurant>>, t: Throwable
                ) {
                    t.printStackTrace()
                }
            })
    }

    /**
     * This method will allow us to retrieve the restaurants that were favorited upon
    process death
     */
    private fun List<Restaurant>.restoreSelections():
            List<Restaurant> {
        stateHandle.get<List<Int>?>(FAVORITES)?.let {
                selectedIds ->
            val restaurantsMap = this.associateBy { it.id }
            selectedIds.forEach { id ->
                restaurantsMap[id]?.isFavorite = true
            }
            return restaurantsMap.values.toList()
        }
        return this
    }

    // cancel any ongoing work before viewModel is destroyed to prevent memory leak
    override fun onCleared() {
        super.onCleared()
        restaurantsCall.cancel()
    }

    companion object {
        const val FAVORITES = "favorites"
    }


}