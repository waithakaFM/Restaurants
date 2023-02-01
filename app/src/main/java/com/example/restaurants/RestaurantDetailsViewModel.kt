package com.example.restaurants

/**
 * Behind the scenes, the Navigation component saves the navigation arguments
stored in NavStackEntry into SavedStateHandle, which our VM exposes.
This means that we can take advantage of that, and instead of obtaining the ID of
the restaurant inside the RestaurantDetailsScreen() composable, we can
directly obtain it in RestaurantDetailsViewModel.
This approach will protect us from system-initiated process death scenarios as well.
 */
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

/**
 * Responsible of requesting restaurant details for the second screen
 */
class RestaurantDetailsViewModel(
    private val stateHandle: SavedStateHandle
): ViewModel() {
    private var restInterface: RestaurantsApiService
    val state = mutableStateOf<Restaurant?>(null)

    init {
        val retrofit: Retrofit = Retrofit.Builder()
            .addConverterFactory(
                GsonConverterFactory
                .create())
            .baseUrl("https://restaurants-5cacf-default-rtdb.firebaseio.com/")
            .build()
        restInterface = retrofit.create(
            RestaurantsApiService::class.java)

        val id = stateHandle.get<Int>("restaurant_id") ?: 0
        viewModelScope.launch {
            val restaurant = getRemoteRestaurant(id)
            state.value = restaurant
        }
    }

    /**
     * Receives an id parameter and takes care of executing the network
    request to get the details of a specific restaurant
     */
    private suspend fun getRemoteRestaurant(id: Int):
            Restaurant {
        return withContext(Dispatchers.IO) {
            val responseMap = restInterface
                .getRestaurant(id)
            return@withContext responseMap.values.first()
        }
    }
}