package com.example.restaurants

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.launch
import retrofit2.HttpException
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.net.ConnectException
import java.net.UnknownHostException

/**
 * Responsible of requesting restaurant details for the second screen
 */
class RestaurantDetailsViewModel(private val stateHandle: SavedStateHandle): ViewModel() {

    private val repository = RestaurantsRepository()

    val _state = mutableStateOf(
        RestaurantDetailsState(
            restaurant = null,
            title = null,
            description = null
        )
    )

    val state: State<RestaurantDetailsState>
        get() = _state

    private val errorHandler =
        CoroutineExceptionHandler { _, exception ->
            exception.printStackTrace()
            _state.value = _state.value.copy(
                error = exception.message,
                isLoading = false
            )
        }

    init {
        val id = stateHandle.get<Int>("restaurant_id") ?: 0
        viewModelScope.launch(errorHandler) {
            val restaurant = repository.getRemoteRestaurant(id)
            _state.value = _state.value.copy(
                restaurant = restaurant,
                isLoading = false
            )
        }
    }
}