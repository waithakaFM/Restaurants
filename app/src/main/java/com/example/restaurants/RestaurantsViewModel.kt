package com.example.restaurants

import androidx.compose.runtime.State
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.*


class RestaurantsViewModel(): ViewModel() {

    private val repository = RestaurantsRepository()

    // backing property: making the _state only exposed to the viewModel
    private val _state = mutableStateOf(
        RestaurantsScreenState(
            restaurants = listOf(),
            isLoading = true
        )
    )

   // Part of the public API
    val state: State<RestaurantsScreenState>
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
        getRestaurants()
    }

    /**
     * we passed the restaurants from the local database, which were obtained from
    the toggleFavoriteRestaurant() function
     */
    fun toggleFavorite(id: Int, oldValue: Boolean) {
        viewModelScope.launch(errorHandler) {
            val updatedRestaurants = repository.toggleFavoriteRestaurant(id, oldValue)
            _state.value = _state.value.copy( restaurants = updatedRestaurants)
        }
    }

    /**
     * We first stored restaurants inside a restaurants variable. Then, we used
    the copy() function to pass a new restaurants list that we received to the
    restaurants field,
     */
    private fun getRestaurants() {
        viewModelScope.launch(errorHandler) {
            val restaurants = repository.getAllRestaurants()
            _state.value = _state.value.copy(
                restaurants = restaurants,
                isLoading = false
            )
        }
    }



}