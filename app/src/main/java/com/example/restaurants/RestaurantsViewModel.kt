package com.example.restaurants

import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel

// TODO: Hoist the state to make sure ViewModel is the only source of truth for our state.
class RestaurantsViewModel(
    //TODO: use a SavedStateHandle object to recover from system-initiated process death
    private val stateHandle: SavedStateHandle):
    ViewModel() {
    val state = mutableStateOf(
        dummyRestaurants.restoreSelections())

    /**
     * this  will allow theRestaurantsViewModel to mutate the value of the state variable every time
    we try to toggle the favorite status of a restaurant
     */
    fun toggleFavorite(id: Int) {
        // TODO: Obtain the current list of restaurants
        val restaurants = state.value.toMutableList()
        // TODO: Obtained the index of the item whose isFavorite field should be updated
        val itemIndex =
            restaurants.indexOfFirst { it.id == id }
        //TODO: Obtained the item object of type Restaurant
        val item = restaurants[itemIndex]
        restaurants[itemIndex] =
            item.copy(isFavorite = !item.isFavorite)
        //TODO: Call a storeSelection method whenever we toggle the favorite status of a restaurant
        storeSelection(restaurants[itemIndex])
        state.value = restaurants
    }

    /**
     * This method receives a Restaurant object whose isFavorite property has just been altered,
    and saves that selection inside the SavedStateHandle object provided by the
    RestaurantsViewModel class
     */
    private fun storeSelection(item: Restaurant) {
        //TODO: Obtain a list containing the IDs of the previously favorited restaurants
        val savedToggled = stateHandle
            .get<List<Int>?>(FAVORITES)
            .orEmpty().toMutableList()
        // TODO: If this restaurant was marked as favorite, it adds the ID of the restaurant to the
        //  savedToggle list. Otherwise, it removes it.
        if (item.isFavorite) savedToggled.add(item.id)
        else savedToggled.remove(item.id)
        // TODO: Saves the updated list of favorited restaurants with the FAVORITES key inside
        //  the stateHandle map
        stateHandle[FAVORITES] = savedToggled
    }
    // define a constant value for the key used to save the restaurant's selection inside our stateHandle map
    companion object {
        const val FAVORITES = "favorites"
    }

    /**
     * This method will allow us to retrieve the restaurants that were favorited upon
    process death
     */
    private fun List<Restaurant>.restoreSelections():
            // TODO: obtaining the list with the unique identifiers of the previously favorited
    //  restaurants from stateHandle
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
}